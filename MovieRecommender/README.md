## Movie Recommender
In order to run the MovieRecommender project in **Eclipse**, download the following dependencies<br>
#### JNI_SVMLight
A java impementation to the SVM light library. <br>
[JNI_SVM-light-6.01-64bit](http://mpi-inf.mpg.de/~mtb/svmlight/JNI_SVM-light-6.01-64bit.zip)<br>

#### RapidMiner
A java based data mining API. <br>
[RapidMiner v.5.0](http://rapid-i.com/content/view/398/243/lang,en/)

***
#### Build Path
To configure **Eclipse** project build path <br>
 _Build Path -> Configure Build Path -> Libraries -> Add External JARs_ <br>
Navigate to the following jars <br>
<ul>
<li> <b>jnisvmlight.jar</b> (from <b>JNI_SVM-light-6.01-64bit.zip</b>)</li>
<li> <b>rapidminer.jar</b> (from <b>RapidMiner</b> installation directory)</li>
<li> <b>rmx_text-*.jar</b> (from <b>RapidMiner</b> plugins directory -platform dependent-)</li>
</ul>
Add the **JNI_SVM-light-6.01-64bit/lib** directory to _Native Library Location_ of **jnisvmlight.jar**<br>
Extend _jnisvmlight.jar_ -> _Native Library Location_ -> _Edit_

***
#### Running
Change the path in the following two parameters to reflect the absolute paths to the training set pos and neg directories<br>
`<list key="text_directories">`<br>
          `<parameter key="pos" value="/home/sshihata/Development/workspace/Intel-ML-internship/MovieRecommender/training set/pos"/>`<br>
          `<parameter key="neg" value="/home/sshihata/Development/workspace/Intel-ML-internship/MovieRecommender/training set/neg"/>`<br>
 `</list>`<br>
Run **SVMRecommender.java** for the SVM based implementation.
