import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns


with open('data/deltaTimes.out') as f:
    deltaTimes = f.readlines()


freqs = []

time = 1 
acumTime = 0
collisions = 0
totalCollisions = 0
maxTime = 200
for i in range(len(deltaTimes)):
    if acumTime > maxTime:
        break
    if acumTime >= time:
        freqs.append(collisions)
        collisions = 0
        time += 1
        
    acumTime += float(deltaTimes[i])
    collisions += 1
    totalCollisions += 1

#plt.style.use('ggplot')
plt.rcParams["patch.force_edgecolor"] = True

#fig, ax  = plt.subplots()
#weights = np.ones_like(freqs) / (len(freqs))
#ax.hist(freqs, bins = 20, weights = weights)

print(freqs)
ax = sns.distplot(freqs, bins = 25)

#plt.savefig('graph.png', format='png', bbox_inches = 'tight', dpi = 100)


# ========================================================================================
plt.show()