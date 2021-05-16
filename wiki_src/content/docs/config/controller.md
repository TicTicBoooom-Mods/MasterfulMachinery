---
title: "Step By Step"
description: "Config for defining Controllers and Ports."
lead: "All config driven blocks must bge created in the modpack config folder under the masterful_machinery directory/folder."
date: 2020-10-06T08:49:31+00:00
lastmod: 2020-10-06T08:49:31+00:00
draft: false
images: [  '/assets/images/screenshot.png']
menu:
  docs:
    parent: "config"
weight: 630
toc: true
---

## Why not DataPack For The Blocks?

In minecraft blocks and items are registered a long time before the datapack's are even loaded. This means that to register blocks and items from JSON files, the mod must have a non-datapack-driven way of creating and configuring those blocks and such.

# Creating A Controller Block

Firstly, we must create the block which controls and handles the machines and recipes. This block is called a Controller.

To create a controller block you must navigate to the following directory/folder relative to the `.minecraft` or the equivalent root directory/folder for your modpack.

Path:  `/config/masterful_machinery/controllers`.

Once you are inside the above directory. create a new JSON file, you can call it anything you want as long as it ends in `.json`.

Open the JSON file in your favorite JSON supporting text editor

