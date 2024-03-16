package experiments;

import utils.Factor;
import wrsn.Map;
import wrsn.Sensor;
import wrsn.WCE;

import java.util.ArrayList;

// Starvation Avoidance Mobile Energy Replacement scheme
public class FCFS extends Algorithm {
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
        nextChargingNode = nextChargingSensorNodeSelection(S, requests); // tinh toan nut sac tiep theo
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

    public ArrayList<Integer> getChargedNodes() {
        return chargedNodes;
    }

    public void setChargedNodes(ArrayList<Integer> chargedNodes) {
        this.chargedNodes = chargedNodes;
    }

    public ArrayList<Integer> getDeadNodes() {
        return deadNodes;
    }

    public void setDeadNodes(ArrayList<Integer> deadNodes) {
        this.deadNodes = deadNodes;
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

    private int nextChargingSensorNodeSelection(ArrayList<Integer> S, Request[] requests) {
        int poolCapacity = S.size(); // kich thuoc service pool
        if (poolCapacity != 0) {
            return S.get(0);    // ai den truoc phuc vu truoc
        }
        else return -1;
    }

    private void delete(int i, ArrayList<Integer> S, Request[] requests) {
        S.remove(Integer.valueOf(i));
        requests[i] = null;
    }
}
