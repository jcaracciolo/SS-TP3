import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

def frequencies(dir, show = False):
    with open(f'{dir}/deltaTimes.out') as f:
        deltaTimes = f.readlines()


    freqs = []

    time = 1 
    acumTime = 0
    collisions = 0
    totalCollisions = 0
    maxTime = 200

    agg = 7
    ct = 0
    acum = 0
    for i in range(len(deltaTimes)):
        if ct == agg:
            ct = 0
            acum = acum / agg
            if 1/acum < 5000:
                freqs.append(1/acum)
            acum = 0
        ct += 1
        acum += float(deltaTimes[i])
        
    #plt.style.use('ggplot')
    plt.rcParams["patch.force_edgecolor"] = True

    #fig, ax  = plt.subplots()
    #weights = np.ones_like(freqs) / (len(freqs))
    #ax.hist(freqs, bins = 20, weights = weights)

    ax = sns.distplot(freqs, bins = 50)

    if not show:
        plt.savefig(f'images/{dir}/frequenciesDist.png', format='png', bbox_inches = 'tight', dpi = 100)


# ========================================================================================
    if show:
        plt.show()
    plt.close()