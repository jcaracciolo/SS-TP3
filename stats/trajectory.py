import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

def trajectory(dir, show = False):
    with open(f'{dir}/BigParticle-trajectory.dat') as f:
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


    if show:
        plt.show()
    else:
        plt.savefig(f'images/{dir}/bigTrajectory.png', format='png', bbox_inches = 'tight', dpi = 100)
    


def smallTrajectory(dir, show = False):
    with open('data/SmallParticle_0-trajectory.dat') as f:
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
    if show:
        plt.show()
    else:
        plt.savefig(f'images/{dir}/smallTrajectory.png', format='png', bbox_inches = 'tight', dpi = 100)
    
    plt.close()