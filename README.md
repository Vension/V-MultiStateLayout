<p align="center">
   <a href="https://bintray.com/vension/vensionCenter/MultiStateLayout/_latestVersion">
    <img src="https://img.shields.io/badge/Jcenter-V1.0.0-brightgreen.svg?style=flat-square" alt="Latest Stable Version" />
  </a>
  <a href="https://travis-ci.org/Vension/V-MultiStateLayout">
    <img src="https://travis-ci.org/Vension/V-MultiStateLayout.svg?branch=master" alt="Build Status" />
  </a>
  <a href="https://developer.android.com/about/versions/android-4.0.html">
    <img src="https://img.shields.io/badge/API-15%2B-blue.svg?style=flat-square" alt="Min Sdk Version" />
  </a>
  <a href="http://www.apache.org/licenses/LICENSE-2.0">
    <img src="http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square" alt="License" />
  </a>
  <a href="https://www.jianshu.com/u/38adb0e04e65">
    <img src="https://img.shields.io/badge/Author-Vension-orange.svg?style=flat-square" alt="Author" />
  </a>
  <a href="https://shang.qq.com/wpa/qunwpa?idkey=1a5dc5e9b2e40a780522f46877ba243eeb64405d42398643d544d3eec6624917">
    <img src="https://img.shields.io/badge/QQ-2506856664-orange.svg?style=flat-square" alt="QQ Group" />
  </a>
</p>



## Preview
<p>
    <img src="https://github.com/Vension/V-MultiStateLayout/blob/master/ScreenShot/GIF.gif" width="30%" height="30%">
</p>



## Download
# V-MultiStateLayout [ ![Download](https://api.bintray.com/packages/vension/vensionCenter/MultiStateLayout/images/download.svg) ](https://bintray.com/vension/vensionCenter/MultiStateLayout/_latestVersion)
``` gradle
 implementation 'me.vension:MultiStateLayout:_latestVersion'
```

## Usage

* **具体使用查看demo示例**
```java
 MultiStateLayout.Builder(this)
            .initPage(this)
            .setOnRetryListener(object : MultiStateLayout.OnRetryClickListener{
                override fun onRetry() {
                    //TODO 点击重试
                }
            })
            .build()
            .showLoading()
```

## update
* **V1.0.0**: <初始化版本>


## About Me
* **Email**: <2506856664@qq.com>
* **Github**: <https://github.com/Vension>
* **简书**: <https://www.jianshu.com/u/38adb0e04e65>

## License
```
 Copyright 2018, Vension

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

