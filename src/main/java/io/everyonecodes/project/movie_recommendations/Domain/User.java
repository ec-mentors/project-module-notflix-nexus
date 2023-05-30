package io.everyonecodes.project.movie_recommendations.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {

        @Id
        private Long id;

        @NotNull
        @Column(unique = true)
        private String username;
        private String password;
        private Set<String> authorities;
        private WatchedList watchedList;

        public User() {
        }

        public User(Long id, String username, String password, Set<String> authorities, WatchedList watchedList) {
                this.id = id;
                this.username = username;
                this.password = password;
                this.authorities = authorities;
                this.watchedList = watchedList;
        }

        @Override
        public String toString() {
                return "User{" +
                        "id=" + id +
                        ", username='" + username + '\'' +
                        ", password='" + password + '\'' +
                        ", authorities=" + authorities +
                        ", watchedList=" + watchedList +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                User user = (User) o;
                return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(authorities, user.authorities) && Objects.equals(watchedList, user.watchedList);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, username, password, authorities, watchedList);
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
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

        public WatchedList getWatchedList() {
                return watchedList;
        }

        public void setWatchedList(WatchedList watchedList) {
                this.watchedList = watchedList;
        }
}