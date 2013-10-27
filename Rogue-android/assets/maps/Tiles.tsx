<?xml version="1.0" encoding="UTF-8"?>
<tileset name="Tiles" tilewidth="32" tileheight="32">
 <image source="spriteSheet.png" width="96" height="128"/>
 <terraintypes>
  <terrain name="New Terrain" tile="-1"/>
 </terraintypes>
 <tile id="1">
  <properties>
   <property name="blocked" value="0"/>
  </properties>
 </tile>
 <tile id="2">
  <properties>
   <property name="blocked" value="0"/>
   <property name="direction" value="up"/>
   <property name="stairs" value="1"/>
  </properties>
 </tile>
 <tile id="3">
  <properties>
   <property name="blocked" value=""/>
  </properties>
 </tile>
 <tile id="4">
  <properties>
   <property name="blocked" value="0"/>
  </properties>
 </tile>
 <tile id="5">
  <properties>
   <property name="blocked" value="0"/>
   <property name="direction" value="down"/>
   <property name="stairs" value="1"/>
  </properties>
 </tile>
 <tile id="6">
  <properties>
   <property name="blocked" value="0"/>
  </properties>
 </tile>
 <tile id="9">
  <properties>
   <property name="blocked" value="1"/>
  </properties>
 </tile>
</tileset>
