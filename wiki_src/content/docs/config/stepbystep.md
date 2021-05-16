---
title: "Controller & Ports Tutorial"
description: "Config for defining Controllers and Ports."
lead: "All config driven blocks must bge created in the modpack config folder under the masterful_machinery directory/folder."
date: 2020-10-06T08:49:31+00:00
lastmod: 2020-10-06T08:49:31+00:00
draft: false
images: []
menu:
  docs:
    parent: "config"
weight: 630
toc: true
---

## Why not DataPack For The Blocks?

In minecraft blocks and items are registered a long time before the datapack's are even loaded. This means that to register blocks and items from JSON files, the mod must have a non-datapack-driven way of creating and configuring those blocks and such.

## Creating A Controller

Firstly, we must create the block which controls and handles the machines and recipes. This block is called a Controller.

To create a controller block you must navigate to the following directory/folder relative to the `.minecraft` or the equivalent root directory/folder for your modpack.

Path:  `/config/masterful_machinery/controllers`.

Once you are inside the above directory. create a new JSON file, you can call it anything you want as long as it ends in `.json`.

## Writing the JSON File

Firstly, inside of our newly created json file we must set a unique identifier for the controller:

```json
{
  "controllerId": "basic"
}
```

Controller blocks and items are created with the registry id of the following format: `masterfulmachinery:[controllerId]_controller`.

Using this format we know that our controller will be created with the block and item ids of `masterfulmachinery:basic_controller`

Next we must create a more human-readable name for our controller.
To do this amend the previous JSON to match the following.
```json
{
  "controllerId": "basic",
  "name": "Basic"
}
```

Similarly to the id, the name is created in a defined format. The format for the name is as follows: `[name] Controller`.

From this we know that the name of our controller with be `Basic Controller`

However, the file is not ready to be used yet, to make it "bare-minimum" usable (a single controller block) we must add an empty array for our ports.
To do this amend the previous JSON to match the following.

```json
{
  "controllerId": "basic",
  "name": "Basic",
  "ports": []
}
```

> The above file is now able to be loaded by the mod and will function as a very useless controller block.

## Defining Ports

Ports are a block which allow a player to input and output all sorts of different resources of many different types to and from machines.

To create ports which are usable to a player, we must add an entry to our `ports` array.
To do this amend the previous JSON to match the following.

```json
{
  "controllerId": "basic",
  "name": "Basic",
  "ports": [
    {

    }
  ]
}
```

In it's current state the empty JSON object will crash the game, but it is a good demonstration of JSON and it's format.

Next we must define a type field, this will tell the mod what type of port we want to create (item/fluid/energy/etc.) within the JSON object.
To do this amend the previous JSON to match the following.

```json
{
  "controllerId": "basic",
  "name": "Basic",
  "ports": [
    {
      "type": "masterfulmachinery:items"
    }
  ]
}
```

The type defined above is that of an item port, this is similar to a chest and will contain slots which can store stacks of items.

> To see a list of available port types [Click Here](/docs/config/types)

Next we must set a unqiue id for our port, this is is relative to the port type and controller.
To do this amend the previous JSON to match the following.

```json
{
  "controllerId": "basic",
  "name": "Basic",
  "ports": [
    {
      "type": "masterfulmachinery:items",
      "id": "small"
    }
  ]
}
```

Port block/item ids are formatted as follows: `masterfulmachinery:[controllerId]_[id]_port_[type]_[input/output]`

With this in mind we know the above ports will have the ids of:
- `mastefulmachinery:basic_small_port_items_input`
- `mastefulmachinery:basic_small_port_items_output`

Keep in mind the fact that 1 port definition will make 2 blocks and items, 1 for input, 1 for output.

Also keep in mind that the id of a port only has to be unique within the same port type and controller, so if you have 3 port with different types they could have the same id. Also if you have 2 different controllers with a port of the same type each,  you can give those the same id ad they will not clash due to the rather lengthy concatination of the name.

Next we must define a human-readable name for our Port. 
To do this amend the previous JSON to match the following.

```json
{
  "controllerId": "basic",
  "name": "Basic",
  "ports": [
    {
      "type": "masterfulmachinery:items",
      "id": "small",
      "name": "Small"
    }
  ]
}
```

The ingame name of the block will be formatted as follows: `[controller name] - [port name] [port type name] Input/Output`. with this in mind we know that the port blocks will be created with the following names for the above config:
- `Basic - Small Item Input`
- `Basic - Small Item Output`

The next section gets a little confusing due to the dynamic nature of the mod. We must define the limits/config/data for the ports we want to create.

Different port types have different fields within the `data` JSON object. For item ports we can do the following:


```json
{
  "controllerId": "basic",
  "name": "Basic",
  "ports": [
    {
      "type": "masterfulmachinery:items",
      "id": "small",
      "name": "Small",
      "data": {
        "rows": 3,
        "columns": 4
      }
    }
  ]
}
```

This will create both input and outputs ports of this type and tier have a chest layout of 3 rows and 4 columns.

> For each port type's data section's fields, [Click Here](/docs/config/types/#controllers-file-config-data-section)

Now run the game with this config and you should get a result of 1 `Basic Controller` and 2 Port blocks with chest like inventories (when clicked) of 3 rows and 4 columns.

For m,ore/larger examples [Click Here](/docs/config/types/#controllers-file-config-data-section)