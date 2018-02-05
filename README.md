# gltf-loader

Loader for [glTF2.0](https://github.com/KhronosGroup/glTF) files written in kotlin. 
The project uses [Klaxon](https://github.com/cbeust/klaxon) to parse JSON.


## Features

* Loading .gltf json files and .bin buffer files (v2.0 only)

## Usage

```kotlin
val asset = GltfAsset.fromFile("assets/BoxInterleaved/Box.gltf")
```

`GltfAsset` is a higher level representation of the data present in the json file. The main difference is that
objects hold actual references to other objects rather than the indices of these object in an centralized array.
Those shared objects all have an `index` field which is their position in the array centralizing the resources of
the same type.

It makes navigation easier without loosing the benefit of having those resources centralized.

Attributes with a defined range of allowed values are replaced by enums holding the original constant values.

```kotlin
val componentTypeConstantValue = ComponentType.FLOAT.code // 5126 
```

## Todos

* Loading .glb file
* Handle embedded base64 buffers

