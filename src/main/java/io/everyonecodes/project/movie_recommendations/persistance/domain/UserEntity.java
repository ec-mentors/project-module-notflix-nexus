package io.everyonecodes.project.movie_recommendations.persistance.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class UserEntity {

        @Id
        @Column(columnDefinition = "BINARY(16)")
        @GeneratedValue
        private UUID id;

//        @Id
//        @Column(name = "userId", columnDefinition = "BINARY(16)")
//        @GeneratedValue
//        private UUID userId;

        @NotBlank
        @Column(unique = true)
        private String username;
        @NotBlank
        private String password;
        @ElementCollection(fetch = FetchType.EAGER)
        private Set<String> authorities;
        @OneToOne(fetch = FetchType.EAGER)
        private WatchList watchList = new WatchList();

        @Override
        public String toString() {
                return "User{" +
                        "id=" + id +
                        ", username='" + username + '\'' +
                        ", password='" + password + '\'' +
                        ", authorities=" + authorities +
                        ", watchList=" + watchList +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                UserEntity user = (UserEntity) o;
                return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(authorities, user.authorities) && Objects.equals(watchList, user.watchList);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, username, password, authorities, watchList);
        }

        public UUID getId() {
                return id;
        }

        public void setId(UUID id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public Set<String> getAuthorities() {
                return authorities;
        }

        public void setAuthorities(Set<String> authorities) {
                this.authorities = authorities;
        }

        public WatchList getWatchList() {return watchList;}

        public void setWatchList(WatchList watchList) {
                this.watchList = watchList;
        }

}