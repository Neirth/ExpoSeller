

<p align="center">
<img src="https://github.com/Neirth/ExpoSeller/blob/main/docs/exposeller_logo.png?raw=true" width="400" height="400" />
</p>
Ticket sales automation system based on Firebase Cloud Storage. From the user interface to the validator using the user's mobile device as a medium.

## Deployment

This is a Github template project, that is, you can be inspired by this project to create your own automatic system that manages the entire life cycle of a ticket for an event such as a concert, a show or anything that requires a ticket.

## Why this proyect?

Actually, this project was born from the need to have to expose something with which I could evaluate and pass my high school in order to enter the university. Well, I thought of something with real world value. Perhaps as a developer you personally prefer that it does not have so much of elements dependent on a screen and more elements with which you can make physical gestures. This project sought to propose this alternative to the jury that was going to evaluate me. 

The document where I explain all this in detail is in [docs/TfcDocument.pdf](https://github.com/Neirth/ExpoSeller/blob/main/docs/TfcDocument.pdf), only in Spanish. :(

## How works this project?

This project is based on the principles of the Clean Architecture book, in which he explains the entire principle of hexagonal architecture.
![](https://github.com/Neirth/ExpoSeller/raw/main/docs/screenshots/screenshot_one.png)


Without going into detail about how this architecture works, a series of interfaces has been exposed to be able to implement them and use our own hardware. These belong to the outermost layer of the software. There are several examples to know how to implement them.

As for the components used in this system, there are mainly 3: The user interface, the mobile device and the ticket validator.

The first will provide us with a graphical interface where we can execute the entire purchase process of the ticket in question, being able to also consult the tickets and everything related. The second will allow us to store the ticket that we have generated. This must be borne in mind that we will never know what operating system it works with, we are only interested in making it compatible with Apple Passbook. Finally, the validator device will allow us to validate the QR generated from the ticket identification code, not through the download QR code, this in turn will invalidate it in the database so that it cannot be used again.

## Screenshots
Here are some screenshots of the user interface:
![](https://github.com/Neirth/ExpoSeller/raw/main/docs/screenshots/screenshot_two.png)
![](https://github.com/Neirth/ExpoSeller/raw/main/docs/screenshots/screenshot_three.png)
![](https://github.com/Neirth/ExpoSeller/raw/main/docs/screenshots/screenshot_four.png)

## Getting Started

### Prerequisites
In order to even start testing this project we will need the following:
- Have a webcam connected and configured
- Have Android Studio installed
- Have installed IntelliJ Community Edition, or any other IDE for Java
- Java 11 at least, in addition to having JavaFX installed and configured.
- Have an account in firebase (Or failing that in Google)
- (Optional, but important) Have an Apple developer account to generate a valid certificate for tickets based on Apple Passbook.
- I don't think I'm forgetting anything. Ok, the shopping list is perfect. (Joke)

### Building

To build this project, you will need, if you want it to go on your appreciated iPhone or iPod, generate the corresponding developer certificate, as I have not yet learned how to generate it, you can use this Apple guide for it: [Wallet Developer Guide: Building Your First Pass](https://developer.apple.com/library/archive/documentation/UserExperience/Conceptual/PassKit_PG/YourFirst.html#//apple_ref/doc/uid/TP40012195-CH2-SW27).

Once the certificate is generated, you will need to convert it to a PKCS#12 file and store it in [assets/certificates/Certificates.p12](https://github.com/Neirth/ExpoSeller/blob/main/src/ExpoSellerClient/app/src/main/assets/certificates/Certificates.p12) within the graphical interface. If you have configured a password, you must include it in [PassbookTicketGeneratorImpl.java](https://github.com/Neirth/ExpoSeller/blob/9c1634685170264e0fbc8a16be2144d27c93e3dd/src/ExpoSellerClient/app/src/main/java/io/smartinez/exposeller/client/peripherals/ticketgenerator/PassbookTicketGeneratorImpl.java#L143) on line 143.

You will also need to generate a Firebase project, associate the credentials with the user interface project and import the rules from the database available in rules/. The guys at Google put a good tutorial at our fingertips so that this can be simple and painless: [Add Firebase to your Android project](https://firebase.google.com/docs/android/setup). You will also have to configure the file [application.properties](https://github.com/Neirth/ExpoSeller/blob/main/src/ExpoSellerChecker/src/main/resources/application.properties) of the validator with the project ID of Firebase.

After configuring our project with our infrastructure. We will only have to build the projects and execute them. The development environment will allow us to easily do this step.

## Built with

The technologies that I have used for this project are:  
- JavaFX  
- OpenCV  
- Firebase  
- OkHttp 3  
- Android Things  
- Apple Passbook  
- Dagger and Weld SE  

Although there are technologies that are mandatory, such as dependency injection, there are others that can be easily replaced, such as JavaFX. Think that in the case of JavaFX we only use it to check the status of the validation, and this can be replaced by an electronic circuit, you know.  

In the case of Android Things, yes, I am aware that this platform was deprecated at the time that this project was quite advanced. This is a big annoyance, but the world is not going to end, you can use Embemmed Android or EmtriaOS to continue using the same features. (I would appreciate if some would try harder to enter markets such as the internet of things) (*Wink*, *Wink*)

## What future awaits this project?
This project was born with the purpose of overcoming the end of high school. Although it is an interesting project, it will be rare that I keep updating it, since it is also thought that other people who go through the same situation that I went through, have a project to be inspired.

At the same time, this project had in mind to leave a functional system for the entire automated management of the ticket life cycle. Therefore, any institution could, technically, take this project and launch it into production.

Everything that can be expected from the future of this project is included in the [MIT license](https://choosealicense.com/licenses/mit/)  with which this project and all its files are attached.

## License
[MIT](https://choosealicense.com/licenses/mit/)
