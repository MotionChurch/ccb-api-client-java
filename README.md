# ccb-api-client-java

ccb-api-client-java is a Java library for accessing the
[Church Community Builder][CCB] [API][APIDOCS].

This library is not provided nor supported by Church Community Builder.
It was build by Jesse Morgan for use in the [Foursquare GROW][GROW] and other
projects and released with the hopes of helping anyone else using the CCB API.

Only a few services are currently supported. The client currently supports:

* `individual_profiles`
* `individual_profile_from_id`
* `individual_profile_from_login_password`
* `individual_profile_from_micr`
* `custom_field_labels`
* `update_individual`
* `group_profiles`
* `group_profile_from_id`
* `campus_list`
* List Lookup Tables

Adding new services is relatively easy. Skip down to the Contributing section
if the one you need is missing.

## Usage

1. If you're using Maven or Ivy, add a dependency on the [latest release](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.p4square%22%20AND%20a%3A%22ccbapi%22):

   ```xml
   <dependency>
      <groupId>com.p4square</groupId>
      <artifactId>ccbapi</artifactId>
      <version>1.0</version>
  </dependency>
   ```

2. Create a single instance of CCBAPIClient for your application.
CCBAPIClient is thread-safe and manages its own pool of HTTP connections.

   ```java
   CCBAPI ccbClient = new CCBAPIClient("mychurch", "myuser", "mypassword");
   ```

3. Call the necessary APIs. For example, to get an individual by id:

   ```java
   try {
       GetIndividualProfilesRequest request = new GetIndividualProfilesRequest()
               .withIndividualId(48);
       GetIndividualProfilesResponse response = mAPI.getIndividualProfiles(request);
   } catch (CCBRetryableErrorException e) {
       // TODO: Retry if necessary with an appropriate back-off.
   } catch (CCBErrorResponseException e) {
       // TODO: Optionally handle error responses from CCB differently than below.
   } catch (IOException e) {
       // TODO: Handle other errors.
   }
   ```

4. Do something useful with the responses from CCB.

   ```java
   IndividualProfile profile = response.getIndividuals().get(0);
   System.out.println(profile.getFullName());
   ```

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request

### Adding New Services

1. Create the request class. The request class should use a builder pattern to
   collect all of the required and optional parameters. For an example, see
   [GetIndividualProfilesRequest.java](src/main/java/com/p4square/ccbapi/model/GetIndividualProfilesRequest.java).

2. Create classes for the response and other models. This is really the hardest
   part. For an example see [GetCustomFieldLabelsResponse.java](src/main/java/com/p4square/ccbapi/model/GetCustomFieldLabelsResponse.java)
   and [CustomField.java](src/main/java/com/p4square/ccbapi/model/CustomField.java).

3. Add tests for binding the models:

    1. Create an XML file from the sample response in the API documentation.
       For an example, see [ccb_custom_field_labels_response.xml](src/test/resources/com/p4square/ccbapi/model/ccb_custom_field_labels_response.xml).

    2. Add a test to verify all the fields bind correctly.
       See [GetCustomFieldLabelsResponseTest.java](src/test/java/com/p4square/ccbapi/model/GetCustomFieldLabelsResponseTest.java).

4. Add methods for the new service to [CCBAPI.java](src/main/java/com/p4square/ccbapi/CCBAPI.java)
   and [CCBAPIClient.java](src/main/java/com/p4square/ccbapi/CCBAPIClient.java).

4. Add tests for your new service to [CCBAPIClientTest.java](src/test/java/com/p4square/ccbapi/CCBAPIClientTest.java).

## License

Copyright (c) 2016-2018 Jesse Morgan

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


[CCB]: https://www.churchcommunitybuilder.com/
[APIDOCS]: https://support.churchcommunitybuilder.com/customer/portal/articles/640589-api-documentation
[GROW]: https://github.com/PuyallupFoursquare/foursquare-grow
