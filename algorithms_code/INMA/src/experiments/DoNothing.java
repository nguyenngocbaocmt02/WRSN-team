package experiments;

import utils.Factor;
import wrsn.Map;
import wrsn.Sensor;

public class DoNothing extends Algorithm {
    @Override
    public void execute(Map map, Factor factor) {
        int N = map.getN(); // so chieu bai toan
        int deadNodeCount = 0;
        for (int i = 0; i < N; i++) {
            Sensor s = map.getSensor(i);
            s.calculateE(Factor.T);
            if (s.getE() < Sensor.E_MIN) deadNodeCount ++;
        }
        System.out.println("So nut chet " + deadNodeCount);
    }
}
