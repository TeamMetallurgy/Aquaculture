# [![Aquaculture 2](http://cf.way2muchnoise.eu/60028.svg "Aquaculture 2") Aquaculture 2](https://www.curseforge.com/minecraft/mc-mods/aquaculture)

This is the official repository and issue-tracker for Aquaculture 2
- An enhancement of Minecraft’s piscatorial system. The catch is always a surprise. Each biome harbors countless fish that are exclusive to its environment, and a plethora of new loot items await you as you cast your line.

Need Help?
======
- Please report any bugs on our [Github](https://github.com/TeamMetallurgy/Aquaculture/issues)!

|You can also find us on Discord<br>
|:------------:|
|<a href="https://discord.gg/TuJXw7V"><img src="https://i.imgur.com/iHi4Ubv.png" alt="Join us on Discord!"  width="200" height="68"></a>|
<br>

How to get Aquaculture through maven
======
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
