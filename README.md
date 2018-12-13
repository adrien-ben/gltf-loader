# gltf-loader

[![Build Status](https://travis-ci.org/adrien-ben/gltf-loader.svg?branch=develop)](https://travis-ci.org/adrien-ben/gltf-loader)
[ ![Download](https://api.bintray.com/packages/adrien-ben/default/gltf-loader/images/download.svg) ](https://bintray.com/adrien-ben/default/gltf-loader/_latestVersion)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a0537889cdb547189543b986a1adfbfc)](https://www.codacy.com/app/adrien.bennadji/gltf-loader?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=adrien-ben/gltf-loader&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/a0537889cdb547189543b986a1adfbfc)](https://www.codacy.com/app/adrien.bennadji/gltf-loader?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=adrien-ben/gltf-loader&amp;utm_campaign=Badge_Coverage)

Loader for [glTF2.0](https://github.com/KhronosGroup/glTF) files written in kotlin. 
The project uses [Klaxon](https://github.com/cbeust/klaxon) to parse JSON.


## What it does

**gltf-loader** loads glTF file. 

## Features

- loads .gltf files
- loads .glb files
- loads external .bin files
- decodes base64 embedded buffers
- decodes base64 embedded textures
- minimal validation

## Usage

```kotlin
val gltf = GltfAsset.fromFile("pathTo/asset.gltf")
val glb = GltfAsset.fromFile("pathTo/asset.glb")
```

> Note that file extension is important because **gltf-loader** selects the proper loader implementation 
> depending on the file extension

## Concept

**gltf-loader** provides a higher level representation of the data present in the gltf files. The main difference is 
that objects hold actual references to other objects rather than the indices of these object in an centralized array.
Those shared objects all have an `index` field which is their position in the array centralizing the resources of the 
same type. It makes navigation easier without loosing the benefit of having those resources centralized.

In the following example `buffer` is an object and not an index :

```kotlin
val buffer = asset.bufferViews[0].buffer
```

But you can retrieve its index :

```kotlin
val bufferIndex = buffer.index
```

Attributes with a defined range of allowed values are replaced by enums holding the original constant values.

```kotlin
val componentTypeConstantValue = ComponentType.FLOAT.code // 5126 
```

## Validation

After loading the file **gltf-loader** performs a minimal validation pass on loaded data. For now it only checks
that data respect the [json schemas](https://github.com/KhronosGroup/glTF/tree/master/specification/2.0/schema).

## Buffers

Buffer data is loaded alongside the json descriptor so clients don't have to. This includes buffers contained
in external .bin files and embedded base64 buffers. Base 64 buffers are decoded when the file is loaded.

## Images 

Embedded base64 image data is decoded too but external image files are not loaded.

## Nodes' Transforms

In gltf files, as stated in [gltf's specification](https://github.com/KhronosGroup/glTF/blob/master/specification/2.0/README.md#transformations) 
: *any node can define a local space transformation either by supplying a  matrix property, or any of translation, rotation, and scale properties*. 
When a node contains a matrix, **gltf-loader** will automatically extract the translation, scale and rotation properties even if their are not defined
in the original file.

## Todos

* Extensions support (pbrSpecularGlossiness)
