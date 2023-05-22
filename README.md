# [Aquaculture 2](https://minecraft.curseforge.com/projects/aquaculture)

An enhancement of Minecraft’s piscatorial system. The catch is always a surprise. Each biome harbors countless fish that are exclusive to its environment, and a plethora of new loot items await you as you cast your line.

How to get Aquaculture through maven
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
  compile fg.deobf("com.teammetallurgy.aquaculture:aquaculture2_${mc_version}:${mc_version}-${aquaculture_version}")
}
```

`${mc_version}` & `${aquaculture_version}` can be found [here](http://girafi.dk/maven/com/teammetallurgy/aquaculture/), check the file name of the version you want.
