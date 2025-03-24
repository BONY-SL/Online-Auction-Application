# BidZone - Online Auction Platform

BidZone is an online auction platform developed using Java. This platform offers a secure and efficient environment for users to engage in buying and selling items through online auctions. By leveraging advanced technologies and robust system architecture, BidZone aims to facilitate a dynamic marketplace where transactions are conducted seamlessly and securely.

## Features and Functionalities

BidZone provides a comprehensive suite of features designed for a seamless auction experience:

- **Item Listing**: Users can list items for auction by providing detailed descriptions, setting initial bids, and uploading images.
- **Bidding**: A live bidding environment that updates bid statuses in real-time, ensuring constant user engagement and excitement.
- **Auction Management**: Administrators can manage auction timings, categorize items, and assign user roles to ensure platform efficiency.
- **User Authentication**: A secure login and registration system to authenticate all user interactions and safeguard user data.
- **Transaction Processing**: Efficient handling of bid transactions and auction outcomes with a focus on accuracy and reliability.

## Software Requirements

- **Operating System**: 
  - Windows 10, Windows 11, MacOS Mojave, or any modern Linux distribution.
  
- **Java Runtime Environment (JRE)**: 
  - Java 17 or newer.

- **Web Browser**: 
  - Latest version of Chrome, Firefox, Safari, or Edge.

- **Database Server**: 
  - MySQL Server 8.0 or higher.

- **Application Server**: 
  - Apache Tomcat 9.0 or similar.

## Frameworks and Technologies

BidZone is built with the following frameworks and technologies to ensure scalability and functionality:

- **Hibernate ORM**: For object-relational mapping, simplifying database interactions and ensuring data integrity.
- **Spring Framework**: Provides infrastructure support for developing Java applications, managing security, transactions, and configurations.
- **JavaScript Bootstrap**: For creating a dynamic, responsive user interface on the client-side.

## Project Setup

### Clone the Repository

To get started with the project, clone the repository using the following command:

```bash git clone https://github.com/BONY-SL/Online-Auction-Application


![75f94855dfa81c611fbc237d6fe6d8042b685d00](https://github.com/BONY-SL/Online-Auction-Application/assets/143308037/ca4a925d-54b9-44b6-afd6-cf0ca5888144)
![7fa3dc98f9d4fc2eb7fe74ef1f36ef3e88a090d7](https://github.com/BONY-SL/Online-Auction-Application/assets/143308037/a8930232-ee88-4b7e-9579-a7300890f4c4)
![ce40de1895651c3f64f4429c31f8d3edd99aafa0](https://github.com/BONY-SL/Online-Auction-Application/assets/143308037/26cdb70c-a197-4a0c-8b31-ac4d76c39675)
![11ca88093d3af472a8d42d3ca8b5b0ff7c37f889](https://github.com/BONY-SL/Online-Auction-Application/assets/143308037/cd06c120-81c7-49af-a45c-83d0bc542267)
![e2b6b6c53be2dd4c1be106447c29ce7ddbfb7202](https://github.com/BONY-SL/Online-Auction-Application/assets/143308037/744d81cb-d007-4d32-bd92-aa1ba0e56a5e)
![87d2103d13f9f0f64b563bb2dc6f8d7e8716468b](https://github.com/BONY-SL/Online-Auction-Application/assets/143308037/b8e019a7-ccd6-473c-9721-93ccfe4f69da)

Code Examples User Profile Manage




    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile(@RequestParam(value = "username", required = false) String username, Principal principal) {
        String effectiveUsername;
        System.out.println(3);
        if (username != null && !username.isEmpty()) {
            effectiveUsername = username;
        } else if (principal != null) {
            effectiveUsername = principal.getName();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        UserProfileDTO profile = profileService.getProfile(effectiveUsername);
        return ResponseEntity.ok(profile);
    }



    @GetMapping("/profile/{profileId}")
    public UserProfileDTO getProfile(@PathVariable Long profileId) throws ProfileNotFoundException {
        System.out.println("Profile ID: " + profileId);
        return profileService.getProfile(profileId);
    }


    @PatchMapping(value = "/profile", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateProfile(
            @RequestPart("profile") final UserProfileUpdateDTO profileUpdateRequest,
            @RequestPart(required = false) final MultipartFile image,
            @RequestParam("username") String username) {

        System.out.println(profileUpdateRequest);
        System.out.println(image);
        System.out.println(username);

        try {
            UserProfileDTO updatedProfile = profileService.updateProfile(profileUpdateRequest, image, username);
            return ResponseEntity.ok(updatedProfile);
        } catch (CommonAppExceptions e) {
            HttpStatus status = e.getHttpStatus();
            return ResponseEntity.status(status).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file");
        }
    }





    public UserProfileDTO updateProfile(final UserProfileUpdateDTO profileUpdateRequest, final MultipartFile image, final String userName) throws IOException {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new CommonAppExceptions("Unknown user", HttpStatus.NOT_FOUND));
        final UserProfile existingProfile = user.getUserProfile();
        modelMapper.map(profileUpdateRequest, existingProfile);

        // Save profile picture
        if (image != null && !image.isEmpty()) {
            final String imageFilePath = saveFileToDisk(image, userName);
            existingProfile.setProfilePictureS3URL(imageFilePath);
        }
        final UserProfile savedProfile = profileRepository.save(existingProfile);

        UserProfileDTO dto = convertToDto(savedProfile);
        dto.setProfilePictureURL(savedProfile.getProfilePictureS3URL());
        return dto;
    }


    /*in this method create for the upload to the image to database the database saved image path url the image actualy
    saved in the local disk connect the database link path
    */
    public String saveFileToDisk(final MultipartFile image, String userName) throws IOException {
        String targetDir = "C:\\Users\\Public\\uploads\\images\\" + userName;
        File directory = new File(targetDir);

        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + targetDir);
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        File targetFile = new File(directory, fileName);

        try {
            image.transferTo(targetFile);
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new IOException("Failed to save file to disk", e);
        }

        return "/uploads/images/" + userName + "/" + fileName;
    }
