package com.geekster.Blogging.API.service;

import com.geekster.Blogging.API.model.*;
import com.geekster.Blogging.API.model.security.SignInput;
import com.geekster.Blogging.API.model.security.SignUpOutput;
import com.geekster.Blogging.API.repository.UserRepo;
import com.geekster.Blogging.API.service.hashUtility.PasswordEncrypter;
import com.geekster.Blogging.API.service.mail.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    public SignUpOutput UserSignUp(User user) {
        String getCurrentEmail=user.getUserEmail();
        if(getCurrentEmail==null)
        {
            return new SignUpOutput(false,"Invalid Email");
        }
        User checkEmail=userRepo.findFirstByUserEmail(getCurrentEmail);
        if(checkEmail !=null)
        {
            return new SignUpOutput(false,"User already exist please signIn :");
        }
        try
        {
            String encryptPassword=PasswordEncrypter.passwordEncrypt(user.getUserPassword());
            user.setUserPassword(encryptPassword);
            userRepo.save(user);
            return new SignUpOutput(true,"signUp successfully");
        }
        catch (Exception e)
        {
            return new SignUpOutput(false,"Internal server error");
        }
    }

    public String userSignIn(SignInput signInput) {
        String checkEmail=signInput.getEmail();
        if(checkEmail==null)
        {
            return "Invalid email";
        }
        User user=userRepo.findFirstByUserEmail(checkEmail);
        if(user==null)
        {
            return "please signUp";
        }
        try
        {
            String encryptPassword=PasswordEncrypter.passwordEncrypt(signInput.getPassword());
            if(signInput.getPassword().equals(encryptPassword))
            {
                AuthenticationToken authenticationToken=new AuthenticationToken(user);
                authenticationService.saveAuthentication(authenticationToken);

                SendMail.sendEmail(signInput.getEmail(),"mail testing",authenticationToken.getTokenValue());
                return "token send successfully";
            }
            else
            {
                return "Invalid credentials";
            }
        }
        catch (Exception e)
        {
            return "Internal Server Error";
        }
    }

    public String userSignOut(String email) {
        User user=userRepo.findFirstByUserEmail(email);
        AuthenticationToken authenticationToken=authenticationService.findFirstByUser(user);
        authenticationService.removeToken(authenticationToken);
        return "User Signed out successfully";
    }

    public String createBloggingPost(Post post, String email) {
        User user=userRepo.findFirstByUserEmail(email);
        post.setPostOwner(user);
        return postService.createBloggingPost(post);
    }

    public String deletePost(Long postId, String email) {
        User user=userRepo.findFirstByUserEmail(email);
        return postService.removeBloggingPost(postId,user);
    }

    public String addComment(Comment comment, String commenterEmail) {
        boolean postValid = postService.validatePost(comment.getInstaPost());
        if(postValid) {
            User commenter = userRepo.findFirstByUserEmail(commenterEmail);
            comment.setCommenter(commenter);
            return commentService.addComment(comment);
        }
        else {
            return "Cannot comment on Invalid Post!!";
        }
    }

    boolean authorizeCommentRemover(String email,Comment comment)
    {
        String  commentOwnerEmail = comment.getCommenter().getUserEmail();
        String  postOwnerEmail  = comment.getInstaPost().getPostOwner().getUserEmail();

        return postOwnerEmail.equals(email) || commentOwnerEmail.equals(email);
    }
    public String removeBloggingComment(Integer commentId, String email) {
        Comment comment  = commentService.findComment(commentId);
        if(comment!=null)
        {
            if(authorizeCommentRemover(email,comment))
            {
                commentService.removeComment(comment);
                return "comment deleted successfully";
            }
            else
            {
                return "Unauthorized delete detected...Not allowed!!!!";
            }

        }
        else
        {
            return "Invalid Comment";
        }
    }
    public String followUser(Follow follow, String followerEmail) {


        User followTargetUser = userRepo.findById(follow.getCurrentUser().getUserId()).orElse(null);

        User follower = userRepo.findFirstByUserEmail(followerEmail);

        if(followTargetUser!=null)
        {
            if(followService.isFollowAllowed(followTargetUser,follower))
            {
                followService.startFollowing(follow,follower);
                return follower.getUserHandle()  + " is now following " + followTargetUser.getUserHandle();
            }
            else {
                return follower.getUserHandle()  + " already follows " + followTargetUser.getUserHandle();
            }
        }
        else {
            return "User to be followed is Invalid!!!";
        }
    }
    private boolean authorizeUnfollow(String email, Follow follow) {

        String  targetEmail = follow.getCurrentUser().getUserEmail();
        String  followerEmail  = follow.getCurrentUserFollower().getUserEmail();

        return targetEmail.equals(email) || followerEmail.equals(email);
    }

    public String unFollowUser(Integer followId, String followerEmail) {

        Follow follow  = followService.findFollow(followId);
        if(follow != null)
        {
            if(authorizeUnfollow(followerEmail,follow))
            {
                followService.unfollow(follow);
                return follow.getCurrentUser().getUserHandle() + "not followed by " + followerEmail;
            }
            else
            {
                return "Unauthorized unfollow detected...Not allowed!!!!";
            }

        }
        else
        {
            return "Invalid follow mapping";
        }
    }


}
