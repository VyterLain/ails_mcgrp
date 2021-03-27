# ails_mcgrp

##### Routing问题学习——论文的复现

- 论文名：An Adaptive Iterated Local Search for the Mixed Capacitated General Routing Problem

- 作者名：Mauro Dell’Amico, José Carlos Díaz Díaz, Geir Hasle, Manuel Iori

- 期刊名：Transportation Science

##### 关于MCGRP/NEARP：

- 在一个混合图中，部分点、边、弧需要被服务且只服务一次，从一个depot发出若干有容量限制的车辆进行服务

##### 关于代码：

- 代码运行为```main```
- *local search*算子`flip`，`swap`，`or-opt`，`2-opt`，`3-opt`
- *perturb*算子7个`destroy`，3个`repair`

- 使用的算例为benchmark——`bhw`，`cbmix`

##### 联系方式

mg20150013@smail.nju.edu.cn