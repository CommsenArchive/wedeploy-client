if mvn clean && mvn -Pwedeploy-client,site; then
	currentDir=$(pwd)
	cd ../../wedeploy-client-site
	git add . && git commit -m "Update site" && git push
	cd $currentDir
fi

