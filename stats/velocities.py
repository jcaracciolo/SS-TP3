import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

def velocities(dir, show = False):
    with open(f'{dir}/lastVelocities.dat') as f:
        velocities = f.readlines()


    samples = []

    time = 1 
    acumTime = 0
    collisions = 0
    totalCollisions = 0
    maxTime = 200
    for i in range(0, len(velocities), 2):
        vs = velocities[i].split(' ')
        for i in range(len(vs) - 1):
            samples.append(float(vs[i]))

    #plt.style.use('ggplot')
    plt.rcParams["patch.force_edgecolor"] = True
    plt.xlim((0,0.5))
    plt.ylim((0,18))
    #fig, ax  = plt.subplots()
    #weights = np.ones_like(freqs) / (len(freqs))
    #ax.hist(freqs, bins = 20, weights = weights)

    #print(samples)
    ax = sns.distplot(samples, bins = 50)

    if not show:
        plt.savefig(f'images/{dir}/lastVelocities.png', format='png', bbox_inches = 'tight', dpi = 100)


    # ========================================================================================
    if show:
        plt.show()
    
    plt.close()