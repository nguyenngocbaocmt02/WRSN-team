import math

from simpful import *


class FuzzyCS:
    def __init__(self):
        self.FS = FuzzySystem()
        self.initialize()

    def initialize(self):
        FS = self.FS
        U_1 = FuzzySet(function=Trapezoidal_MF(a=0, b=0, c=0.15, d=0.45), term='low')
        U_2 = FuzzySet(function=Trapezoidal_MF(a=0.15, b=0.45, c=0.75, d=0.85), term='moderate')
        U_3 = FuzzySet(function=Trapezoidal_MF(a=0.75, b=0.85, c=1, d=1), term='high')
        LV1 = LinguisticVariable([U_1, U_2, U_3], concept='U', universe_of_discourse=[0, 1])
        FS.add_linguistic_variable('U', LV1)

        ESR_1 = FuzzySet(function=Trapezoidal_MF(a=0, b=0, c=0.2, d=0.25), term='low')
        ESR_2 = FuzzySet(function=Trapezoidal_MF(a=0.2, b=0.25, c=0.45, d=0.55), term='moderate')
        ESR_3 = FuzzySet(function=Trapezoidal_MF(a=0.45, b=0.55, c=1, d=1), term='high')
        LV2 = LinguisticVariable([ESR_1, ESR_2, ESR_3], concept='ESR', universe_of_discourse=[0, 1])
        FS.add_linguistic_variable('ESR', LV2)

        E_1 = FuzzySet(function=Trapezoidal_MF(a=0, b=0, c=0.15, d=0.5), term='low')
        E_2 = FuzzySet(function=Trapezoidal_MF(a=0.15, b=0.5, c=0.75, d=0.85), term='moderate')
        E_3 = FuzzySet(function=Trapezoidal_MF(a=0.75, b=0.85, c=1, d=1), term='high')
        LV3 = LinguisticVariable([E_1, E_2, E_3], concept='E', universe_of_discourse=[0, 1])
        FS.add_linguistic_variable('E', LV3)

        SAFE_0 = FuzzySet(function=Trapezoidal_MF(a=0, b=0, c=0.05, d=0.15), term='exlow')
        SAFE_1 = FuzzySet(function=Trapezoidal_MF(a=0.05, b=0.15, c=0.2, d=0.3), term='verylow')
        SAFE_2 = FuzzySet(function=Trapezoidal_MF(a=0.2, b=0.3, c=0.35, d=0.45), term='low')
        SAFE_3 = FuzzySet(function=Trapezoidal_MF(a=0.35, b=0.45, c=0.5, d=0.6), term='moderate')
        SAFE_4 = FuzzySet(function=Trapezoidal_MF(a=0.5, b=0.6, c=0.65, d=0.75), term='high')
        SAFE_5 = FuzzySet(function=Trapezoidal_MF(a=0.65, b=0.75, c=0.8, d=0.9), term='veryhigh')
        SAFE_6 = FuzzySet(function=Trapezoidal_MF(a=0.8, b=0.9, c=1, d=1), term='exhigh')

        LV4 = LinguisticVariable([SAFE_0, SAFE_1, SAFE_2, SAFE_3, SAFE_4, SAFE_5, SAFE_6], concept='SAFE',
                                 universe_of_discourse=[0, 1])
        FS.add_linguistic_variable('SAFE', LV4)

        # Define fuzzy rules
        R1 = 'IF (U IS low) AND (ESR IS low) AND (E IS low) THEN (SAFE IS exlow)'
        R2 = 'IF (U IS low) AND (ESR IS low) AND (E IS moderate) THEN (SAFE IS exlow)'
        R3 = 'IF (U IS low) AND (ESR IS low) AND (E IS high) THEN (SAFE IS exlow)'
        R4 = 'IF (U IS low) AND (ESR IS moderate) AND (E IS low) THEN (SAFE IS moderate)'
        R5 = 'IF (U IS low) AND (ESR IS moderate) AND (E IS moderate) THEN (SAFE IS moderate)'
        R6 = 'IF (U IS low) AND (ESR IS moderate) AND (E IS high) THEN (SAFE IS moderate)'
        R7 = 'IF (U IS low) AND (ESR IS high) AND (E IS low) THEN (SAFE IS veryhigh)'
        R8 = 'IF (U IS low) AND (ESR IS high) AND (E IS moderate) THEN (SAFE IS veryhigh)'
        R9 = 'IF (U IS low) AND (ESR IS high) AND (E IS high) THEN (SAFE IS veryhigh)'

        R10 = 'IF (U IS moderate) AND (ESR IS low) AND (E IS low) THEN (SAFE IS exlow)'
        R11 = 'IF (U IS moderate) AND (ESR IS low) AND (E IS moderate) THEN (SAFE IS verylow)'
        R12 = 'IF (U IS moderate) AND (ESR IS low) AND (E IS high) THEN (SAFE IS verylow)'
        R13 = 'IF (U IS moderate) AND (ESR IS moderate) AND (E IS low) THEN (SAFE IS moderate)'
        R14 = 'IF (U IS moderate) AND (ESR IS moderate) AND (E IS moderate) THEN (SAFE IS high)'
        R15 = 'IF (U IS moderate) AND (ESR IS moderate) AND (E IS high) THEN (SAFE IS high)'
        R16 = 'IF (U IS moderate) AND (ESR IS high) AND (E IS low) THEN (SAFE IS veryhigh)'
        R17 = 'IF (U IS moderate) AND (ESR IS high) AND (E IS moderate) THEN (SAFE IS exhigh)'
        R18 = 'IF (U IS moderate) AND (ESR IS high) AND (E IS high) THEN (SAFE IS exhigh)'

        R19 = 'IF (U IS high) AND (ESR IS low) AND (E IS low) THEN (SAFE IS exlow)'
        R20 = 'IF (U IS high) AND (ESR IS low) AND (E IS moderate) THEN (SAFE IS verylow)'
        R21 = 'IF (U IS high) AND (ESR IS low) AND (E IS high) THEN (SAFE IS low)'
        R22 = 'IF (U IS high) AND (ESR IS moderate) AND (E IS low) THEN (SAFE IS moderate)'
        R23 = 'IF (U IS high) AND (ESR IS moderate) AND (E IS moderate) THEN (SAFE IS high)'
        R24 = 'IF (U IS high) AND (ESR IS moderate) AND (E IS high) THEN (SAFE IS veryhigh)'
        R25 = 'IF (U IS high) AND (ESR IS high) AND (E IS low) THEN (SAFE IS veryhigh)'
        R26 = 'IF (U IS high) AND (ESR IS high) AND (E IS moderate) THEN (SAFE IS exhigh)'
        R27 = 'IF (U IS high) AND (ESR IS high) AND (E IS high) THEN (SAFE IS exhigh)'

        FS.add_rules(
            [R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15, R16, R17, R18, R19, R20, R21, R22, R23,
             R24, R25, R26, R27])

    def operate(self, net, T, mc):
        safeEnergy = []
        maxESR = 0.0001
        sumESR = 0
        aliveNode = 0
        sumE = 0
        for node in net.listNodes:
            if node.status == 0:
                continue
            aliveNode += 1
            maxESR = max(maxESR, node.energyCR)
            sumESR += node.energyCR
            sumE += node.energy

        sumECharge = mc.capacity - (net.baseStation.location[0] * 2) * math.sqrt(2) / 5 * aliveNode
        print(str((mc.alpha / (mc.beta ** 2)) / (sumESR)) + '  ' + str(mc.capacity / (sumESR * T)))
        for node in net.listNodes:
            if node.status == 0:
                safeEnergy.append(0)
            else:
                self.FS.set_variable('U', (mc.alpha / (mc.beta ** 2)) / (sumESR))
                self.FS.set_variable('ESR', node.energyCR / ((node.capacity - node.threshold) / T))
                self.FS.set_variable('E', mc.capacity / (sumESR * T))
                # Perform Madman inference and print output
                safeEnergy.append(
                    ((self.FS.Mamdani_inference(['SAFE'])['SAFE']) * (node.capacity - node.threshold) + node.threshold))
            # print(str(RS) + " " + str(node.energyCR / maxESR)+" "+str(safeEnergy[-1]))
        return safeEnergy
