# VisuWarp
Warp menu spigot plugin, by Juskie

## Getting started - plugin installation
Simply drop the JAR into your server plugins folder.
On first time launch it'll create a config file which will be used to store warps for the warp menu. 

## Building the plugin
If you want to manually build the plugin, just run the gradle build task and you can then run the jar task, which will create
a compiled jar file in the directory: build/libs/VisuWarpSpigot-<version>.jar

## Permissions
The plugin comes with the following permissions (you can set these using any appropriate permissions management plugin):
- visuwarp.*: Grants access to all plugin commands.
- visuwarp.open: Grants access to the warps menu "/vwmenu".
- visuwarp.list: Grants access to command: "/vwmenu list" which lists all warp names in chat.
- visuwarp.version: If granted, allows the user to use "/vwmenu version" which simply lists the plugin version.
- visuwarp.add: Grants the ability to add warps to the warp menu, using: /vwmenu add <warpname> <description>
- visuwarp.remove: Grants the ability to remove warps from the warp menu, using: /vwmenu remove <warpname>

## Command usages
- /vwmenu open - Open the warp menu
- /vwmenu list - List all warps
- /vwmenu add <warpname> <description> - Adds a warp
- /vwmenu remove <warpname> - Remove a warp
- /vwmenu version - Display plugin version
