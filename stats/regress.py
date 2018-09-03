import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import statsmodels.formula.api as sm

with open('other_initial_velocities/BigParticle-DCM.dat') as f:
    dcms = f.readlines()

x1 = []
y1 = []
yerr1 = []
selected_x = []
selected_y = []
selected_err = []
variances = []

i = 0
for i in range(0, len(dcms), 50):
    x_i = float(dcms[i].split(' ')[0])
    y_i = float(dcms[i].split(' ')[1])
    yerr_i = float(dcms[i].split(' ')[2])
    if yerr_i != 0:
        x1.append(x_i)
        y1.append(y_i)
        yerr1.append(yerr_i)
        if x_i > 60:
            selected_x.append(x_i)
            selected_y.append(y_i)
            selected_err.append(yerr_i)
            variances.append(yerr_i ** 2)


ws = pd.DataFrame({
    'Tiempo (s)' : x1,
    'DCM (cm)' : y1,
    'yerrplot' : yerr1,
})

wws = pd.DataFrame({
    'x': selected_x,
    'y': selected_y,
    'yerr': selected_err
})

weights = pd.Series(variances)
print(wws['y'])

wls_fit = sm.wls('y ~ x', data=wws, weights=1 / weights).fit()
ols_fit = sm.ols('y ~ x', data=wws).fit()

print(wls_fit.summary())
print(ols_fit.summary())


plt.clf()
plt.style.use('ggplot')

fig = plt.figure()
ax = fig.add_subplot(111)
ws.plot(
    kind='scatter',
    x='Tiempo (s)',
    y='DCM (cm)',
    yerr = 'yerrplot',
    style='o',
    alpha=1.,
    ax=ax,
    title='Regresion lineal pesada',
    edgecolor='#ff8300',
    s=40
)

# weighted prediction
wp, = ax.plot(
    wws['x'],
    wls_fit.predict(),
    color='#e55ea2',
    lw=1.,
    alpha=1.0,
)

print(wls_fit.params)
print(ols_fit.params)

op, = ax.plot( 
    wws['x'], 
    ols_fit.predict(),
    color='k',
    ls='solid',
    lw=1,
    alpha=1.0,
)


leg = plt.legend(
    (op, wp),
    (f"OLS (r² = {round(ols_fit._results.rsquared, 3)})     D = {round(ols_fit.params['x'],3)}"   , f"WLS (r² = {round(wls_fit._results.rsquared, 3)} )     D = {round(wls_fit.params['x'], 3)}"),
    loc='upper left',
    fontsize=15)

plt.xlim((0, max(x1) + 3))
plt.tight_layout()
fig.set_size_inches(6.40, 5.12)
plt.savefig("so.png", dpi=100, alpha=True)
plt.show()