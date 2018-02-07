# gltf-loader

Loader for [glTF2.0](https://github.com/KhronosGroup/glTF) files written in kotlin. 
The project uses [Klaxon](https://github.com/cbeust/klaxon) to parse JSON.


## What it does ?

This library can load .gltf file and .glb files. 

The library provides a higher level representation of the data present in the gltf files. The main difference is 
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

Buffer data is loaded (or decoded for embedded base64 buffers) alongside the json descriptor so clients don't have 
to. Embedded base64 image data is decoded too but external image files are not loaded.

## Usage

```kotlin
val gltf = GltfAsset.fromFile("pathTo/asset.gltf")
val glb = GltfAsset.fromFile("pathTo/asset.glb")
```

> Note that file extension is important because the library selects the proper loader implementation 
> depending on the file extension

## Todos

* Post loading validation
* Extensions support (pbrSpecularGlossiness)
