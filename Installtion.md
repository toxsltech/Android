Use this project as base for starting new android project.
Syncmanagaer and other useful libs are added as submodule in this project.

Follow these steps to get this project with updated Android Libs:

1.Take fresh clone of scripts in master branch from here:
Url:http://192.168.10.42/android/swoop-android-1633.git

2.Use Set Up command to setup Whole project together with android libs:
bash {setup.sh path}"path"

3.To get clone of all directories mentioned in .gitmodules use:
bash {clone-submodules.sh path} 

4.To get update all your existing android libs use:
bash {git-sync-all.sh path}

