# neXtProt - The knowledge resource on human proteins

This is a code repository for the SIB - Swiss Institute of Bioinformatics CALIPHO group neXtProt project

See: https://www.nextprot.org/

#neXtProt commons

This project is for internal use.
It contains classes shared between the loaders pipeline and the web API.

##Deployment in dev
* Commit on develop (jenkins will automatically release a new snapshot version if tests are successfull)

##Release a new version for the prod
* `nxs-fire-and-prepare-next-release.sh ${next-version}` (jenkins will merge develop into master and publish the release of the current pom.xml) 
 
### Release scenario
If pom.xml says 0.3.1-SNAPSHOT and the command `nxs-fire-and-prepare-next-release.sh 0.3.2` is executed:
* Jenkins will release 0.3.1
* 0.3.2-SNAPSHOT will be created on the pom.xml 


## Note 
Never touch master branch, this is used by jenkins.
