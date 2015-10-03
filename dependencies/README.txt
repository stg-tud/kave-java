to be able to build the workspace, you need to generate a local p2 site and set a custom target definition:

1) run "mvn p2:site validate" on this project
2) open "kave.target" (you need to have Eclipse PDE installed)
3) wait until all dependencies are resolved *then* click on "set as target platform".