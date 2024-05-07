Create Online Auction Application Using JAVA Consepts ORM,Threads,Design Patterns withing Spring boot

![75f94855dfa81c611fbc237d6fe6d8042b685d00](https://github.com/BONY-SL/Online-Auction-Application/assets/143308037/ca4a925d-54b9-44b6-afd6-cf0ca5888144)
![7fa3dc98f9d4fc2eb7fe74ef1f36ef3e88a090d7](https://github.com/BONY-SL/Online-Auction-Application/assets/143308037/a8930232-ee88-4b7e-9579-a7300890f4c4)
![ce40de1895651c3f64f4429c31f8d3edd99aafa0](https://github.com/BONY-SL/Online-Auction-Application/assets/143308037/26cdb70c-a197-4a0c-8b31-ac4d76c39675)


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
