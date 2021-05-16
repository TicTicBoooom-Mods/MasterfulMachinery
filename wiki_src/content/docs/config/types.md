---
title: "Port Types"
description: "A list of the available port types in Masterful Machinery."
lead: "A list of the available port types in Masterful Machinery."
date: 2020-10-06T08:49:31+00:00
lastmod: 2020-10-06T08:49:31+00:00
draft: false
images: []
menu:
  docs:
    parent: "config"
weight: 660
toc: true
---

- `masterfulmachinery:items` (Item Ports)
- `masterfulmachinery:fluids` (Fluid Ports)
- `masterfulmachinery:energy` (Forge Energy Ports)
- `masterfulmachinery:mekanism_gas` (Mekanism Gas Ports)
- `masterfulmachinery:mekanism_slurry` (Mekanism Slurry Ports)


## Controllers File Config Data Section

- `masterfulmachinery:items` (Item Ports)

```json
{
    "rows": INTEGER,
    "columns": INTEGER
}
```

Replace `INTEGER` with a whole number (integer) for example: `6` or `9` 

---

- `masterfulmachinery:fluids` (Fluid Ports)
```json
{
    "capacity": INTEGER
}
```

Replace `INTEGER` with a whole number (integer) for example: `10000` or `1000000` 

--- 
- `masterfulmachinery:energy` (Forge Energy Ports)

```json
{
    "capacity": INTEGER
}
```

Replace `INTEGER` with a whole number (integer) for example: `10000` or `1000000` 

---

- `masterfulmachinery:mekanism_gas` (Mekanism Gas Ports)

```json
{
    "capacity": INTEGER
}
```

Replace `INTEGER` with a whole number (integer) for example: `10000` or `1000000` 

---

- `masterfulmachinery:mekanism_slurry` (Mekanism Slurry Ports)
```json
{
    "capacity": INTEGER
}
```

Replace `INTEGER` with a whole number (integer) for example: `10000` or `1000000` 

---