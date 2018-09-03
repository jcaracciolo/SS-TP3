import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import statsmodels.formula.api as sm

with open('data/BigParticle-DCM.dat') as f:
    dcms = f.readlines()

x1 = []
y1 = []
yerr1 = []

i = 0
for i in range(0, len(dcms), 30):
    x_i = float(dcms[i].split(' ')[0])
    y_i = float(dcms[i].split(' ')[1])
    yerr_i = float(dcms[i].split(' ')[2])
    x1.append(x_i)
    y1.append(y_i)
    yerr1.append(yerr_i)


ws = pd.DataFrame({
    'x': x1,
    'y': y1
})
weights = pd.Series(yerr1)

wls_fit = sm.wls('x ~ y', data=ws, weights=1 / weights).fit()
ols_fit = sm.ols('x ~ y', data=ws).fit()

print(wls_fit.summary())
print(ols_fit.summary())

plt.style.use('ggplot')

plt.clf()
fig = plt.figure()
ax = fig.add_subplot(111)
ws.plot(
    kind='scatter',
    x='x',
    y='y',
    yerr = yerr1,
    style='o',
    alpha=1.,
    ax=ax,
    title='Regresion lineal pesada',
    edgecolor='#ff8300',
    s=40
)

# weighted prediction
wp, = ax.plot(
    wls_fit.predict(),
    ws['y'],
    color='#e55ea2',
    lw=1.,
    alpha=1.0,
)

op, = ax.plot(  
    ols_fit.predict(),
    ws['y'],
    color='k',
    ls='solid',
    lw=1,
    alpha=1.0,
)

leg = plt.legend(
    (op, wp),
    ('Ordinary Least Squares', 'Weighted Least Squares'),
    loc='upper left',
    fontsize=15)

plt.tight_layout()
fig.set_size_inches(6.40, 5.12)
plt.savefig("so.png", dpi=100, alpha=True)
plt.show()