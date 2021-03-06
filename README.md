[![Build Status](https://app.travis-ci.com/magujun/javafx-minepark.svg?branch=main)](https://app.travis-ci.com/magujun/javafx-minepark)

# javafx-minepark

<h3 align="center">
     <img width="75px" src="https://user-images.githubusercontent.com/75567460/159811876-e8556b1d-fb47-4b57-a884-4d5294c21f29.png">
    <img width="250px" src="https://user-images.githubusercontent.com/75567460/159811559-b59300f5-4aba-4d1d-b44d-c26b04501986.png">
        <img width="75px" src="https://user-images.githubusercontent.com/75567460/159811641-962e9d84-8160-4969-9328-6d50788359a8.png">
    <br><br>
    <p align="center">
      <a href="#-technology">Technology</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
      <a href="#-setup">Setup</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
      <a href="#-contribute">Contribute</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
      <a href="#-license">License</a>
  </p>
</h3>

## 🔖 About

[![GitHub forks](https://img.shields.io/github/forks/magujun/javafx-minepark?style=social)](https://github.com/magujun/javafx-minepark/network/members/)
[![GitHub stars](https://img.shields.io/github/stars/magujun/javafx-minepark?style=social)](https://github.com/magujun/javafx-minepark/stargazers/)

A <strong>JavaFX</strong> implementation of the classic Minesweeper game with a **[South Park™](https://www.southparkstudios.com/)** theme.

## 🚀 Technology

This project has been developed and tested with the following technologies:

- [Java](https://www.java.com/en/) :: Oracle Java™ Language | OpenJDK JRE
- [JavaFX](https://openjfx.io/) :: JavaFX Framework | OpenJFX SDK
- [...Work in progress](https://github.com/magujun/javafx-minepark)

## ⤵ Setup

These instructions will take you to a copy of the project running on your local machine for testing and development purposes.

```bash
    - git clone https://github.com/magujun/javafx-minepark.git
    - cd javafx-minepark
```    

**NOTE:** You will need to download the javaFX libs to run the application.

The following commands are an example for a GNU/Linux x86_64 environment (Debian-based).
```    
    - sudo apt-get install unzip
    - mkdir tmp/
    - curl -L https://download2.gluonhq.com/openjfx/18/openjfx-18_linux-x64_bin-sdk.zip > tmp/openjfx-18_linux-x64_bin-sdk.zip
    - unzip tmp/openjfx-18_linux-x64_bin-sdk.zip -d tmp/
 ```
 *For other platforms, please check [openJFX.io](https://gluonhq.com/products/javafx/) and download the openjfx SDK for your system.*
 
 **COMPILE**
 ```   
    - mkdir bin/
    - javac -d bin --module-path tmp/javafx-sdk-18/lib --add-modules=javafx.controls,javafx.media,javafx.graphics -classpath bin:tmp/javafx-sdk-18/lib/javafx-swt.jar:tmp/javafx-sdk-18/lib/javafx.base.jar:tmp/javafx-sdk-18/lib/javafx.controls.jar:tmp/javafx-sdk-18/lib/javafx.graphics.jar:tmp/javafx-sdk-18/lib/javafx.media.jar:tmp/javafx-sdk-18/lib/javafx.swing.jar:tmp/javafx-sdk-18/lib/javafx.web.jar src/*
```

**RUN**
```
    - java --module-path tmp/javafx-sdk-18/lib --add-modules=javafx.controls,javafx.media,javafx.graphics -classpath bin:tmp/javafx-sdk-18/lib/javafx-swt.jar:tmp/javafx-sdk-18/lib/javafx.base.jar:tmp/javafx-sdk-18/lib/javafx.controls.jar:tmp/javafx-sdk-18/lib/javafx.graphics.jar:tmp/javafx-sdk-18/lib/javafx.media.jar:tmp/javafx-sdk-18/lib/javafx.swing.jar:tmp/javafx-sdk-18/lib/javafx.web.jar Minepark
```

## 🎓 Who taught?

All the Java and JavaFX classes that led me to develop this project were taught by **[Sarah Foss](https://github.com/sarahfoss)** as part of Okanagan College's Computer Information Systems **COSC-121** Course.

## 🤔 Contribute

- Fork this repository;
- Create a branch with your feature: `git checkout -b my-feature`;
- Commit your changes: `git commit -m 'feat: My new feature'`;
- Push to your branch: `git push origin my-feature`.

After the merge of your pull request is done, you can delete your branch.

## 📝 License

This project is under the MIT license.<br/>
See the [LICENSE](LICENSE) file for more details.

---

<h4 align="center">
  Done with ❤ by <a href="https://www.linkedin.com/in/marcelo-guimaraes-junior/" target="_blank">Marcelo Guimarães Junior.</a><br/>
</h4>
