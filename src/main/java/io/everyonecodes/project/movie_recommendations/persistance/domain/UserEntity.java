package io.everyonecodes.project.movie_recommendations.persistance.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

@Entity
public class UserEntity {

        @Id
        @GeneratedValue
        private Long id;

        @NotBlank
        @Column(unique = true)
        private String username;
        @NotBlank
        private String password;
        @ElementCollection(fetch = FetchType.EAGER)
        private Set<String> authorities;
        @OneToOne(fetch = FetchType.LAZY)
        private WatchList watchList = new WatchList();
        @OneToOne(fetch = FetchType.LAZY)
        //@LazyCollection(LazyCollectionOption.FALSE)
        private LikedMoviesList likedMovies = new LikedMoviesList();

        public void addMovieToWatchList(Movie movie) {watchList.addMovie(movie);}

        @Override
        public String toString() {
                return "UserEntity{" +
                        "id=" + id +
                        ", username='" + username + '\'' +
                        ", password='" + password + '\'' +
                        ", authorities=" + authorities +
                        ", watchList=" + watchList +
                        ", likedMovies=" + likedMovies +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                UserEntity that = (UserEntity) o;
                return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(authorities, that.authorities) && Objects.equals(watchList, that.watchList) && Objects.equals(likedMovies, that.likedMovies);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, username, password, authorities, watchList, likedMovies);
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

        public WatchList getWatchList() {return watchList;}

        public void setWatchList(WatchList watchList) {
                this.watchList = watchList;
        }

        public LikedMoviesList getLikedMovies() {
                return likedMovies;
        }

        public void setLikedMovies(LikedMoviesList likedMovies) {
                this.likedMovies = likedMovies;
        }
}