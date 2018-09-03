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

agg = 20
ct = 0
acum = 0
for i in range(len(deltaTimes)):
    if ct == agg:
        ct = 0
        acum = acum / agg
        freqs.append(1/acum)
        acum = 0
    ct += 1
    acum += float(deltaTimes[i])
       
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