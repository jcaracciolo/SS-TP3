import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns


with open('data/BigParticle-trajectory.dat') as f:
    positions = f.readlines()


x = []
y = []

for i in range(len(positions)):
    x.append(float(positions[i].split(' ')[0]))
    y.append(float(positions[i].split(' ')[1]))
    
plt.style.use('ggplot')
plt.xlim((0, 0.5))
plt.ylim((0, 0.5))
plt.plot(x, y)


# ========================================================================================
plt.show()