from abc import ABC

from optimizer.offlineoptimizer.GraphRL import StatusGraph1
from optimizer.offlineoptimizer.GraphRL.FuzzyCS import FuzzyCS
from optimizer.offlineoptimizer.GraphRL.StatusGraph import StatusGraph
from optimizer.offlineoptimizer.GraphRL.StatusGraph1 import StatusGraphabc
from optimizer.offlineoptimizer.OfflineOptimizer import OfflineOptimizer

class GraphRlOptimizer(OfflineOptimizer, ABC):
    def __init__(self, env, T, testedT):
        OfflineOptimizer.__init__(self, env=env)
        self.checkPoint = 0
        self.T = T
        self.testedT = testedT
        self.fuzzy = FuzzyCS()
        self.Esafe = 8000
        if self.testedT % self.T == 0:
            self.linearDF = (self.Esafe - 800) / (int(self.testedT / self.T) - 1)
        else:
            self.linearDF = (self.Esafe - 800) / int(self.testedT / self.T)

    def schedule(self, mcs, net):
        mc = mcs[0]
        print('Time: ' + str(self.env.now) + ' The number of dead node:' + str(net.countDeadNodes()))
        T = min(self.T, self.testedT - self.env.now)
        Esafe = self.fuzzy.operate(net, self.T, mc)
        if T == self.testedT - self.env.now:
            for i in range(len(Esafe)):
                if net.listNodes[i].status == 0:
                    Esafe[i] = 0
                else:
                    Esafe[i] = 650
        print(Esafe)
        self.checkPoint += self.T
        graph = StatusGraph(net=net, mc=mc, delta=100, T=T, Esafe=Esafe)
        graph.initialize()
        # schedule MC
        for ver in graph.path:
            mc.schedule.append([ver.node.location, ver.chargingTime, [ver.node]])
        mc.schedule.append([net.baseStation.location, 0, []])
        del graph
