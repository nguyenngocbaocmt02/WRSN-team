from abc import ABC
from time import time

from optimizer.offlineoptimizer.GraphRL.FuzzyCS import FuzzyCS
from optimizer.offlineoptimizer.GraphRL.StatusGraph import StatusGraph
from optimizer.offlineoptimizer.OfflineOptimizer import OfflineOptimizer


class GraphRlOptimizer(OfflineOptimizer, ABC):
    def __init__(self, env, T, testedT, trimming=0.8, delta=100, nepisode=30000, reward_factor=100):
        OfflineOptimizer.__init__(self, env=env)
        self.checkPoint = 0
        self.T = T
        self.nepisode = nepisode
        self.testedT = testedT
        self.fuzzy = FuzzyCS()
        self.delta = delta
        self.trimming = trimming
        self.log = []
        self.reward_log = []
        self.runtime = 0
        self.reward_factor = reward_factor

    def schedule(self, mcs, net):
        mc = mcs[0]
        print('Time: ' + str(self.env.now) + ' The number of dead node:' + str(net.countDeadNodes()))
        self.log.append(net.countDeadNodes())
        T = min(self.T, self.testedT - self.env.now)
        t0 = time()
        Esafe = self.fuzzy.operate(net, self.T, mc)
        if T == self.testedT - self.env.now:
            for i in range(len(Esafe)):
                if net.listNodes[i].status == 0:
                    Esafe[i] = 0
                else:
                    Esafe[i] = 600
        t1 = time()
        print(Esafe)
        self.checkPoint += self.T
        graph = StatusGraph(net=net, mc=mc, delta=self.delta, T=T, Esafe=Esafe, trimming=self.trimming,
                            reward_factor=self.reward_factor)
        t2 = time()
        graph.initialize()
        t3 = time()
        self.runtime += t1 - t0 + t3 - t2
        self.reward_log.append(graph.log)
        # schedule MC
        for ver in graph.path:
            mc.schedule.append([ver.node.location, ver.chargingTime, [ver.node]])
            print(str(ver.node.energyCR) + "     " + str(
                ver.node.energy - self.T * ver.node.energyCR + graph.U * ver.chargingTime))
        mc.schedule.append([net.baseStation.location, 0, []])
        del graph
