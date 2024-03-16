import random
import simpy
from matplotlib import pyplot as plt
from iostream.Util import Util
from network.MobileCharger import MobileCharger
from network.Network import Network
from optimizer.offlineoptimizer.GraphRL.GraphRLOptimizer import GraphRlOptimizer

random.seed(1)
util = Util("../data/ga200.txt")
env = simpy.Environment()

net = Network(env=env, listNodes=util.listNodes, baseStation=util.BaseStation)
mc = MobileCharger(index=0, env=env, location=[250, 250])
testedT = 72000
algorithm = GraphRlOptimizer(env=env, T=18000, testedT=testedT, trimming=0.8, reward_factor=100, delta=100)

env.process(mc.operate(net, testedT, algorithm))
env.process(algorithm.controller(mcs=[mc], net=net))
env.process(net.runNetwork(testedT))

env.run(until=testedT)
print(algorithm.log)
print('Time: ' + str(env.now) + ' The number of dead node:' + str(net.countDeadNodes()))
tmp = algorithm.reward_log
temp = []
for num in range(len(tmp[0])):
    avg = 0.0
    for case in tmp:
      avg += case[num]
    if len(tmp) != 0:
      temp.append(avg/(len(tmp)))
plt.plot(temp, label ='Gt')
plt.legend()
plt.show()