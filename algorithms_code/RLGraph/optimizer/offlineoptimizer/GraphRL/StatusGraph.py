import math
import random

from optimizer.offlineoptimizer.GraphRL.Vertex import Vertex


class StatusGraph:
    def __init__(self, net, mc, Esafe, delta=100, T=18000, trimming=0.8, reward_factor=100):
        self.delta = delta
        self.net = net
        self.mc = mc
        self.mapNodeVertexes = [{} for i in range(len(self.net.listNodes))]
        self.U = self.mc.alpha / (self.mc.beta ** 2)
        self.T = T
        self.Esafe = Esafe
        self.path = []
        self.Gt = []
        self.start = Vertex(0, 0, 0)
        self.visited = [False for i in range(len(self.net.listNodes))]
        self.epi = 1
        self.eMC = 0
        self.trimming = trimming
        self.log = []
        self.active_node = []
        self.reward_factor = reward_factor

    def upperNormalize(self, value):
        tmp = int(value / self.delta)
        if tmp * self.delta < value:
            tmp += 1
        return tmp * self.delta

    def lowerNormalize(self, value):
        return int(value / self.delta) * self.delta

    def initialize(self):
        tmp = []
        for node in self.net.listNodes:
            if node.status == 0:
                continue
            if (self.Esafe[node.id] - (node.energy - self.T * node.energyCR)) / self.U > 0:
                self.active_node.append(node)
                continue
            tmp.append([node.id, (node.energy - node.threshold) / node.energyCR])
        choosenList = sorted(tmp, key=lambda item: item[1])
        tmp.clear()
        for i in range(0, int(len(choosenList) * (1 - self.trimming))):
            self.active_node.append(self.net.listNodes[choosenList[i][0]])

        for node in self.active_node:
            # lower bound of arrival time
            lowerBound = self.upperNormalize(
                self.dis(node.location, self.net.baseStation.location) / self.mc.velocity)

            # upper bound of arrival time
            upperBound = self.lowerNormalize(min(self.T - self.dis(node.location,
                                                                   self.net.baseStation.location) /
                                                 self.mc.velocity,
                                                 (node.energy - node.threshold) / node.energyCR))
            upperBound -= self.delta
            tcSafe = (self.Esafe[node.id] - (node.energy - self.T * node.energyCR)) / self.U
            # trimming the tree
            # the node with lots of energy will no need more energy so we will charge full for these node
            if tcSafe <= 0:
                tcSafe = (self.T * node.energyCR) / self.U
            l = lowerBound
            # discrete the time
            while l <= upperBound:
                reward = 0
                endVertex = False
                tc = min(tcSafe,
                         (node.capacity - (node.energy - (l + self.delta) * node.energyCR)) / (self.U - node.energyCR))
                # to fix the charging time if it exceeds T
                if l + self.delta + tc + self.dis(node.location, self.net.baseStation.location) / \
                        self.mc.velocity >= self.T - 1:
                    tc = self.T - self.dis(node.location, self.net.baseStation.location) / \
                         self.mc.velocity - l - self.delta
                    endVertex = True
                # reward obtained by charging
                # reward obtained by charging
                if node.energy - self.T * node.energyCR + tc * self.U > node.threshold:
                    reward = math.pow(self.reward_factor,
                                      tc * self.U / (self.Esafe[node.id] - node.energy + self.T * node.energyCR))
                if node.energy - self.T * node.energyCR <= node.threshold < node.energy - self.T * node.energyCR + tc * self.U:
                    reward += self.reward_factor

                self.mapNodeVertexes[node.id][l] = Vertex(node=node, lowerArrivalTime=l,
                                                          upperArrivalTime=l + self.delta,
                                                          chargingTime=tc, endVertex=endVertex, reward=reward)
                l += self.delta
        for node in self.active_node:
            dic = self.mapNodeVertexes[node.id]
            for vertex in dic.values():
                self.setNeighbors(vertex)
        for node in self.active_node:
            i = node.id
            dic = self.mapNodeVertexes[node.id]
            if self.upperNormalize(
                    self.dis(self.net.listNodes[i].location
                        , self.net.baseStation.location) / self.mc.velocity) in dic.keys():
                self.start.adjacent.append(dic[self.upperNormalize(
                    self.dis(self.net.listNodes[i].location
                             , self.net.baseStation.location) / self.mc.velocity)])
        self.epi = 1

        for i in range(1, 30000):
            if i == 30000:
                self.epi = 0
            self.visited = [False for i in range(len(self.net.listNodes))]
            self.path.clear()
            self.Gt.clear()
            self.eMC = 0
            self.dfs(self.start)
            self.log.append(self.Gt[-1])
            # print(str(i)+" "+str(self.Gt[-1]))
            for j, vertex in enumerate(self.path):
                vertex.N += 1
                vertex.value = vertex.value + 1.0 / vertex.N * (self.Gt[-1] - self.Gt[j] - vertex.value)
            self.epi = max(1 / math.sqrt(i), 0.01)

    def setNeighbors(self, vertex):
        sumTime = 0
        if vertex.endVertex:
            return
        for node in self.active_node:
            if vertex.node.id == node.id:
                continue
            sumTime = vertex.chargingTime + self.dis(node.location,
                                                     vertex.node.location) / self.mc.velocity
            sumTime = self.upperNormalize(sumTime)
            if sumTime + vertex.lowerArrivalTime in self.mapNodeVertexes[node.id].keys():
                vertex.adjacent.append(self.mapNodeVertexes[node.id][vertex.lowerArrivalTime + sumTime])

    def dis(self, a, b):
        return math.sqrt((a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]))

    def dfs(self, vertex):
        if len(vertex.adjacent) == 0:
            return
        if len(self.path) == 0:
            curMC = self.net.baseStation.location
        else:
            curMC = self.path[-1].node.location
        next = None
        tmp = []
        for ver in vertex.adjacent:
            if not self.visited[ver.node.id]:
                tmp.append(ver)
                if next is None:
                    next = ver
                    continue
                if ver.value + ver.reward > next.value + next.reward:
                    next = ver
        if next is None:
            return
        if random.random() < self.epi:
            next = tmp[random.randint(0, len(tmp) - 1)]
        if self.eMC + self.dis(curMC,
                               next.node.location) / self.mc.velocity * self.mc.pm + next.chargingTime * self.U + self.dis(
            self.net.baseStation.location, next.node.location) / self.mc.velocity * self.mc.pm > self.mc.capacity:
            return
        self.path.append(next)
        if len(self.Gt) == 0:
            self.Gt.append(next.reward)
        else:
            self.Gt.append(next.reward + self.Gt[-1])
        self.visited[next.node.id] = True
        self.eMC += self.dis(curMC, next.node.location) / self.mc.velocity * self.mc.pm + next.chargingTime * self.U
        self.dfs(next)
