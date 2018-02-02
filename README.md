# gltf-loader

Loader for [glTF2.0](https://github.com/KhronosGroup/glTF) files written in kotlin. 
The project uses [Klaxon](https://github.com/cbeust/klaxon) to parse JSON.


## Features

* Loading .gltf json files and .bin buffer files (v2.0 only)

## Usage

There are two way to load a .gltf file :

```kotlin
val rawAsset = GltfAssetRaw.fromGltfFile("assets/Box/Box.gltf")
```

`GltfAssetRaw` hold a raw representation of the .glsl json file and a map of `ByteArray` indexed by their uri.

```kotlin
val asset = GltfAsset.fromGltfFile("assets/BoxInterleaved/Box.gltf")
```

`GltfAsset` is a higher level representation of the data. It is very close to `GltfAssetRaw` except objects 
hold actual references to other objects rather than the indices of these object in an centralized array.
Attributes with a defined range of allowed values are replaced by enums holding the original constant values.

```kotlin
val componentTypeConstantValue = ComponentType.FLOAT.code // 5126 
```

For now `GltfAsset` only hold data required for static mesh rendering. Sparse accessor are not supported either.   

## Todos

* Loading .glb file
* Finish implementing `GltfAsset`

