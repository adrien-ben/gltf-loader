# gltf-loader

Loader for [glTF2.0](https://github.com/KhronosGroup/glTF) files written in kotlin. 
The project uses [Klaxon](https://github.com/cbeust/klaxon) to parse JSON.


## Features

* Loading .gltf json files and .bin buffer files (v2.0 only)

## Usage

```kotlin
val asset = GltfAssetRaw.fromGltfFile("assets/Box/Box.gltf")
```

## Todos

* Loading .glb file
* Higher level representation of the data

