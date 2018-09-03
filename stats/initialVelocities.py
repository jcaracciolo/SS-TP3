import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns


with open('data/initialVelocities.dat') as f:
    velocities = f.readlines()


samples = []


vs = velocities[0].split(' ')
for i in range(len(vs) - 1):
    samples.append(float(vs[i]))

#plt.style.use('ggplot')
plt.rcParams["patch.force_edgecolor"] = True

#fig, ax  = plt.subplots()
#weights = np.ones_like(freqs) / (len(freqs))
#ax.hist(freqs, bins = 20, weights = weights)

#print(samples)
ax = sns.distplot(samples, bins = 100, kde=False, norm_hist=True)

#plt.savefig('graph.png', format='png', bbox_inches = 'tight', dpi = 100)


# ========================================================================================
plt.show()