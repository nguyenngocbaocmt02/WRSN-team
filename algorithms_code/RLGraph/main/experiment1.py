import os
import random
import threading
import simpy
from scipy.spatial import distance
from iostream.Util import Util
from network.MobileCharger import MobileCharger
from network.Network import Network
from optimizer.offlineoptimizer.GraphRL.GraphRLOptimizer import GraphRlOptimizer


def runFile(fileName, index):
    util = Util(fileName)
    env = simpy.Environment()
    net = Network(env=env, listNodes=util.listNodes, baseStation=util.BaseStation)
    mc = MobileCharger(index=0, env=env, location=[250, 250])
    testedT = 72000
    algorithm = GraphRlOptimizer(env=env, T=18000, testedT=testedT)

    env.process(mc.operate(net, testedT, algorithm))
    env.process(algorithm.controller(mcs=[mc], net=net))
    env.process(net.runNetwork(testedT))
    env.run(until=testedT)
    results[index].append(net.countDeadNodes())
    log = mc.log
    t1 = 0
    t2 = 0
    now = net.baseStation.location
    for action in log:
        t1 = t1 + distance.euclidean(now, action[0]) / mc.velocity * mc.pm
        t2 = t2 + action[3]
    moving[index].append(t1)
    charging[index].append(t2)
    print('Time: ' + str(env.now) + ' The number of dead node:' + str(net.countDeadNodes()))


def toLine(tF, arr):
    res = tF + '\t'
    avg = 0
    for num in arr:
        res = res + str(num) + '\t'
        avg += num
    if len(arr) != 0:
        avg /= len(arr)
    res = res + str(avg) + '\n'
    return res


def writeTxt():
    resultFile = open(resultFolder + '/' + 'result.txt', 'w')
    movingFile = open(resultFolder + '/' + 'movingEnergy.txt', 'w')
    chargingFile = open(resultFolder + '/' + 'chargingEnergy.txt', 'w')
    for i, line in enumerate(results):
        resultFile.write(toLine(listFile[i], results[i]))
        movingFile.write(toLine(listFile[i], moving[i]))
        chargingFile.write(toLine(listFile[i], charging[i]))
    resultFile.close()
    movingFile.close()
    chargingFile.close()


dataFolder = "../data"
resultFolder = "../result/experiment1"
listFile = os.listdir(dataFolder)
results = []
moving = []
charging = []
seed_num = 1
for i in range(len(listFile)):
    results.append([])
    charging.append([])
    moving.append([])
for seed in range(seed_num):
    random.seed(seed)
    threads = []
    for i, fileName in enumerate(listFile):
        threads.append(threading.Thread(target=runFile, args=(dataFolder + '/' + fileName, i,)))
    for thread in threads:
        thread.start()
    for thread in threads:
        thread.join()
writeTxt()
print(results)
