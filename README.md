# [Aquaculture](https://minecraft.curseforge.com/projects/aquaculture)

An expansion of Minecraft's fishing system. You never know what you'll catch. Every biome is home to tons of unique fish you can only catch in that biome, and there's a myriad of new loot items you can randomly catch while fishing.

How to get Aquaculture (1.12) through maven
---
Add to your build.gradle:
```gradle
repositories {
  maven {
    // url of the maven that hosts Aquacultures files
    url "http://girafi.dk/maven/"
  }
}

dependencies {
  // compile against Aquaculture
  compile fg.deobf("com.teammetallurgy.aquaculture:aquaculture_${mc_version}:${mc_version}-${aquaculture_version}")
}
```

How to get Aquaculture2 (1.14-1.16.x) through maven
---
Add to your build.gradle:
```gradle
repositories {
  maven {
    // url of the maven that hosts Aquacultures files
    url "http://girafi.dk/maven/"
  }
}

dependencies {
  // compile against Aquaculture 2
  compile fg.deobf("com.teammetallurgy.aquaculture:aquaculture2_${mc_version}:${mc_version}-${aquaculture_version}")
}
```

`${mc_version}` & `${aquaculture_version}` can be found [here](http://girafi.dk/maven/com/teammetallurgy/aquaculture/), check the file name of the version you want.