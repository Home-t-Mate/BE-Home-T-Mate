public class ProfileControllerUnitTest {
    @Test
    public void real_profile_조회() {
        //given
        String expectedProfile = "real";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");
        ProfileController controller = new ProfileController(env);
        //when
        String profile = controller.profile();
        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    public void real_profile_없으면_첫_번째가_조회된다() {
        //given
        String expectedProfile = "oauth";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("real-db");
        ProfileController controller = new ProfileController(env);
        //when
        String profile = controller.profile();
        //then
        sertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    public void active_profile_없으면_default가_조회된다() {
        given String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();
        ProfileController controller = new ProfileController(env);
        //whe
        String profile = controller.profile();
        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}
    }

