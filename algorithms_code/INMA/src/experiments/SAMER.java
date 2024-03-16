package experiments;

import utils.Factor;
import wrsn.Map;
import wrsn.Sensor;
import wrsn.WCE;

import java.util.ArrayList;

// Starvation Avoidance Mobile Energy Replacement scheme
public class SAMER extends Algorithm {
    @Override
    public void execute(Map map, Factor factor) {
        int N = map.getN(); // so chieu bai toan
        boolean[] flag = new boolean[N];
        for (int i = 0; i < N; i++) {
            flag[i] = true;
        }
        ArrayList<Integer> S = new ArrayList<>(); // Service pool
        Request[] requests = new Request[N]; // danh sach cac request
        int timestamp = 0;
        map.getWce().RE = WCE.E_MC;
        map.getWce().setState(Factor.IDLE);
        while (timestamp < Factor.T) {
            timestamp += Factor.T_INTERVAL;
            for (int i = 0; i < N; i++) {
                Sensor s = map.getSensor(i);
                s.calculateE(Factor.T_INTERVAL);    // tinh nang luong con lai cua sensor i
                if (s.getE() <= Sensor.E_THRESHOLD) {   // neu nang luong con lai dat den nguong sac
                    Request request = new Request(i, s.getE(), s.getP(), timestamp);
                    if (requests[i] == null)  S.add(i);   // neu sensor i chua tung request
                    requests[i] = request;   // them request moi vao danh sach request
                }
            }

            // Xet trang thai cua WCE
            switch (map.getWce().getState()) {
                case (Factor.IDLE): // WCE dang o trang thai nghi
                    idleTime(S, requests, flag, N, map, timestamp, Factor.T_INTERVAL);
                    break;
                case (Factor.MOVING):
                    movingTime(S, requests, flag, N, map, timestamp, Factor.T_INTERVAL);
                    break;
                case (Factor.CHARGING):
                    chargingTime(chargingNode, S, requests, flag, N, map, timestamp, Factor.T_INTERVAL);
                    break;
                case (Factor.REPLENISH):
                    replenish(S, requests, flag, N, map, timestamp, Factor.T_INTERVAL);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value " + map.getWce().getState());
            }
        }
        for (int i = 0; i < N; i++) {
            double e = map.getSensor(i).getE();
            if (e - Sensor.E_MIN < 0) {
                deadNodes.add(i);
            }
        }
    }

    private void idleTime(ArrayList<Integer> S, Request[] requests, boolean[] flag, int N, Map map, int timestamp, double remainTime) {
        nextChargingNode = nextChargingSensorNodeSelection(S, requests, flag, N, map, timestamp); // tinh toan nut sac tiep theo
        if (nextChargingNode != -1) {
            map.setWCEState(Factor.MOVING);    // chuyen trang thai sang di chuyen
            movingTime(S, requests, flag, N, map, timestamp, remainTime);
        }
    }

    private void movingTime(ArrayList<Integer> S, Request[] requests, boolean[] flag, int N, Map map, int timestamp, double rTime) {
        double remainTime = 0;
        if (map.canReturnBS(nextChargingNode)) {
            //        nextChargingNode = nextChargingSensorNodeSelection(S, requests, flag, N, map, timestamp); // tinh toan nut sac tiep theo
            remainTime = map.wceLocationCalculate(nextChargingNode, rTime); // tinh lai vi tri cua WCE
            // Neu van con trong t_interval ma WCE da den noi (remainTime > 0) chuyen trang thai sac
        }
        else {
            map.setWCEState(Factor.REPLENISH);    // chuyen trang thai sang sac cho MC
            replenish(S, requests, flag, N, map, timestamp, rTime);
        }
        if (map.canReturnBS(nextChargingNode)) {
            if (remainTime > 0) {
                map.setWCEState(Factor.CHARGING);   // chuyen trang thai sang sac
                chargingNode = nextChargingNode;
                chargingTime(chargingNode, S, requests, flag, N, map, timestamp, remainTime);
            }
        }
        else {
            map.setWCEState(Factor.REPLENISH);    // chuyen trang thai sang sac cho MC
            replenish(S, requests, flag, N, map, timestamp, remainTime);
        }
    }

    private void replenish(ArrayList<Integer> S, Request[] requests, boolean[] flag, int N, Map map, int timestamp, double rTime) {
//        nextChargingNode = nextChargingSensorNodeSelection(S, requests, flag, N, map, timestamp); // tinh toan nut sac tiep theo
        double remainTime = map.wceLocationCalculate(Factor.BS_INDEX, rTime); // tinh lai vi tri cua WCE
        // Neu van con trong t_interval ma WCE da den noi (remainTime > 0) chuyen trang thai sac
        if (remainTime > 0) {
            map.getWce().RE = WCE.E_MC;
            map.setWCEState(Factor.IDLE);   // chuyen trang thai sang cho
            idleTime(S, requests, flag, N, map, timestamp, remainTime);
        }
    }

    private void chargingTime(int chargingNode, ArrayList<Integer> S, Request[] requests, boolean[] flag, int N, Map map, int timestamp, double chargingTime) {
        double remainTime = 0;
        if (map.canReturnBS(nextChargingNode)) {
            if (chargedNodes.isEmpty() || chargedNodes.get(nodeCount - 1) != chargingNode) {
            chargedNodes.add(chargingNode);
            nodeCount ++;
        }
            remainTime = map.charging(chargingNode, chargingTime);
        }
        else {
            map.setWCEState(Factor.REPLENISH);    // chuyen trang thai sang sac cho MC
            replenish(S, requests, flag, N, map, timestamp, chargingTime);
        }

        if (map.canReturnBS(nextChargingNode)) {
            // Neu van con trong t_interval ma WCE da sac xong (remainTime > 0) chuyen trang thai cho
            if (remainTime > 0) {
                S.remove(Integer.valueOf(chargingNode));
                requests[chargingNode] = null;
                map.setWCEState(Factor.IDLE);   // chuyen trang thai sang cho
                idleTime(S, requests, flag, N, map, timestamp, remainTime);
            }
        }
        else {
            map.setWCEState(Factor.REPLENISH);    // chuyen trang thai sang sac cho MC
            replenish(S, requests, flag, N, map, timestamp, remainTime);
        }
    }

    private int nextChargingSensorNodeSelection(ArrayList<Integer> S, Request[] requests, boolean[] flag, int N, Map map, int t) {
        ArrayList<Integer> Sigma = new ArrayList<>(); // tap sensor ung cu vien duoc sac
        int poolCapacity = S.size(); // kich thuoc service pool
        double[] T_WCE = new double[N]; // danh sach thoi gian di chuyen của WCE toi node i
        double[] L_t = new double[N];   // danh sach do tre sac chap nhan duoc
        double[][] W_t = new double[N][N]; // danh sach thoi gian cho uoc luong tu node i sang j
        ArrayList<Integer> deleteNodes = new ArrayList<>();

        for (int sensor : S) {
            T_WCE[sensor] = map.distanceCalculate(Factor.WCE_INDEX, sensor) / WCE.V;  // thoi gian di chuyen tu vi tri hien tai cua WCE den node i
            L_t[sensor] = requests[sensor].getRE()/requests[sensor].getP() + requests[sensor].getTs() - t;  // do tre sac toi da chap nhan duoc
            if (T_WCE[sensor] > L_t[sensor]) deleteNodes.add(sensor);  //  neu thoi gian di chuyen vuot qua do tre sac, xoa node do khoi hang cho
            else {
                Sensor s = map.getSensor(sensor);
                if (s.getP() >= WCE.U) deleteNodes.add(sensor);
            }
        }

        for (int sensor : deleteNodes) {
            delete(sensor, S, requests);
        }

        for (int sensor1 : S) {
            for (int sensor2 : S) {
                double t_ij = map.distanceCalculate(sensor1, sensor2) / WCE.V;  // thoi gian di chuyen tu node i sang node j
                // thoi gian cho uoc luong cua node j neu sac node i
                W_t[sensor1][sensor2] = T_WCE[sensor1] + (Sensor.E_MAX - (requests[sensor1].getRE() - (t - requests[sensor1].getTs() + T_WCE[sensor1]) * requests[sensor1].getP())) / WCE.U + t_ij;
                // node i duoc sac ma dan toi it nhat 1 node j chet thi flag = false
                if (W_t[sensor1][sensor2] > L_t[sensor2]) flag[sensor1] = false;
            }
        }

        // luu cac node i co flag = true vao tap ung cu vien (do khong dan toi nut chet nao)
        for (int sensor : S) {
            if (flag[sensor]) Sigma.add(sensor);
        }

        if (!Sigma.isEmpty()) {
            double shortestTime = T_WCE[Sigma.get(0)];
            int index = Sigma.get(0);
            for (int node : Sigma) {
                if (T_WCE[node] < shortestTime) {
                    shortestTime = T_WCE[node];
                    index = node;
                }
            }
            return index;
        }
        else if(!S.isEmpty()) {
            double shortestTime = T_WCE[S.get(0)];
            int index = S.get(0);
            for (int node : S) {
                if (T_WCE[node] < shortestTime) {
                    shortestTime = T_WCE[node];
                    index = node;
                }
            }
            return index;
        }
        else return -1;
    }

    private void delete(int i, ArrayList<Integer> S, Request[] requests) {
        S.remove(Integer.valueOf(i));
        requests[i] = null;
    }
}
