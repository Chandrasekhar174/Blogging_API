package com.geekster.Blogging.API.controller;

import com.geekster.Blogging.API.model.Comment;
import com.geekster.Blogging.API.model.Follow;
import com.geekster.Blogging.API.model.Post;
import com.geekster.Blogging.API.model.User;
import com.geekster.Blogging.API.model.security.SignInput;
import com.geekster.Blogging.API.model.security.SignUpOutput;
import com.geekster.Blogging.API.service.AuthenticationService;
import com.geekster.Blogging.API.service.UserService;
import jakarta.servlet.ServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationService authenticationService;
    @PostMapping("User/signUp")
    public SignUpOutput signUp(@RequestBody @Valid User user)
    {
        return userService.UserSignUp(user);
    }
    @PostMapping("User/signIn")
    public String userSignIn(@RequestBody SignInput signInput)
    {
        return userService.userSignIn(signInput);
    }
    @DeleteMapping("User/signOut")
    public String signOut(String email,String token)
    {
        if(authenticationService.authenticate(email,token))
        {
            return userService.userSignOut(email);
        }
        else
        {
            return "Sign out not allowed for non authenticated user.";
        }
    }
    @PostMapping("post")
    public String BloggingPost(@RequestBody Post post , @RequestParam String email,@RequestParam String token)
    {
        if(authenticationService.authenticate(email,token))
        {
            return userService.createBloggingPost(post,email);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }
    @DeleteMapping("post")
    public String DeletePost(@RequestParam Long postId,@RequestParam String email,@RequestParam String token)
    {
        if(authenticationService.authenticate(email,token))
        {
            return userService.deletePost(postId,email);
        }
        else
        {
            return "Not an Authenticated user activity!!!";
        }
    }
    @PostMapping("comment")
    public String addComment(@RequestBody Comment comment, @RequestParam String commenterEmail, @RequestParam String commenterToken)
    {
        if(authenticationService.authenticate(commenterEmail,commenterToken)) {
            return userService.addComment(comment,commenterEmail);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }
    @DeleteMapping("comment")
    public String removeBloggingComment(@RequestParam Integer commentId, @RequestParam String email, @RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return userService.removeBloggingComment(commentId,email);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }
    @PostMapping("follow")
    public String followUser(@RequestBody Follow follow, @RequestParam String followerEmail, @RequestParam String followerToken)
    {
        if(authenticationService.authenticate(followerEmail,followerToken)) {
            return userService.followUser(follow,followerEmail);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }
    @DeleteMapping("unfollow/target/{followId}")
    public String unFollowUser(@PathVariable Integer followId, @RequestParam String followerEmail, @RequestParam String followerToken)
    {
        if(authenticationService.authenticate(followerEmail,followerToken)) {
            return userService.unFollowUser(followId,followerEmail);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }

}
