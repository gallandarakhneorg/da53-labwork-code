# Run JavaCC

## From command-line

Edit one of the scripts into the folder `javacc_scripts`.

Type on the command line from the labwork folder:

	$> cd ./src/main/java/fr/utbm/info/da53/lw4/parser
	$> ../../../../../../../../javacc_scripts/javacc ./basic.jj

## From Eclipse IDE

The first time you have to launch Java:

1. Open the package the "Maven Dependencies" in the project tree view
2. Right click on the JavaCC dependency
3. Select `Run As` > `Java application...`
4. In the dialog box, select the main class named `javacc` in the default package

The javacc program is running and should fails.

5. Open the menu `Run` > `Run configurations`
6. Select `javacc` in the `Java applications` section
7. Open the tab `Arguments`
8. In the working directory section, enter `${workspace_loc:lw4/src/main/java/fr/utbm/info/da53/lw4/parser}`
9. In the program argument box, enter `basic.jj`

After configuring for the first time your run configuration, you just have to launch it.

*Caution:* We recommend to delete all the Java files in the package `fr.utbm.info.da53.lw4.parser` before launching JavaCC.
