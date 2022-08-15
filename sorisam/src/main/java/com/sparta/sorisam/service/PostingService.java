package com.sparta.sorisam.service;

import com.sparta.sorisam.Dto.PostingLikeUserDto;
import com.sparta.sorisam.Dto.RequestDto.PostingRequestDto;
import com.sparta.sorisam.Dto.RequestDto.PostingUpdateRequestDto;
import com.sparta.sorisam.Dto.ResponseDto.PostingDetailResponseDto;
import com.sparta.sorisam.Dto.ResponseDto.PostingResponseDto;
import com.sparta.sorisam.Model.Posting;
//import com.sparta.sorisam.Repository.LikeRepository;
import com.sparta.sorisam.Model.PostingLike;
import com.sparta.sorisam.Repository.LikeRepository;
import com.sparta.sorisam.Repository.PostingRepository;
import com.sparta.sorisam.Repository.UserRepository;
import com.sparta.sorisam.global.error.exception.EntityNotFoundException;
import com.sparta.sorisam.global.error.exception.ErrorCode;
import com.sparta.sorisam.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostingService {
    private final PostingRepository postingRepository;

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;


    // 게시글 작성
    @Transactional
    public Posting createPosting(PostingRequestDto postingRequestDto, String username, String img, String intro) {
        String title = postingRequestDto.getTitle();
        String contents = postingRequestDto.getContents();
        String filePath = postingRequestDto.getFilePath();

        if (title.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_TITLE);
        } else if (contents.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_CONTENTS);
        } else if (filePath.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_FILEPATH);
        }

        return postingRepository.save(new Posting(postingRequestDto, username, img, intro));
    }


    // 게시글 조회
    public List<PostingResponseDto> getPosting() {
        List<Posting> postings = postingRepository.findAllByOrderByCreatedAtDesc();
        List<PostingResponseDto> listPostings = new ArrayList<>();
        for (Posting posting : postings) {
            List<PostingLikeUserDto> postingLikeUserDtos = new ArrayList<>();
            Long cntPostLike = likeRepository.countByPosting(posting);
            List<PostingLike> postingLikes = likeRepository.findAllByPosting(posting);
            for (PostingLike postingLike : postingLikes){
                PostingLikeUserDto postingLikeUserDto = new PostingLikeUserDto(postingLike);
                postingLikeUserDtos.add(postingLikeUserDto);
            }
            PostingResponseDto postingResponseDto = PostingResponseDto.builder()
                    .posting(posting)
                    .cntPostLike(cntPostLike)
                    .build();
            listPostings.add(postingResponseDto);
        }
        return listPostings;
    }

    //게시글 디테일 조회
    public PostingDetailResponseDto getDetailPosting(Long id) {
        Posting posting =  postingRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.NOTFOUND_POST));
        Long cntPostLike = likeRepository.countByPosting(posting);
        return new PostingDetailResponseDto(posting, cntPostLike);

    }


    // 게시글 수정
    @Transactional
    public void updatePosting(Long id, PostingUpdateRequestDto dto, String userName) {
        if (!(userName.equals(exists(id).getUsername()))) {
            throw new InvalidValueException(ErrorCode.NOT_AUTHORIZED);
        }

        String title = dto.getTitle();
        String contents = dto.getContents();
        String filePath = dto.getFilePath();

        if (title.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_TITLE);
        } else if (contents.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_CONTENTS);
        } else if (filePath.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_FILEPATH);
        }

        Posting existingPosting = exists(id);
        existingPosting.updatePosting(dto.getTitle(), dto.getFilePath(), dto.getContents());
        postingRepository.save(existingPosting);
    }

    // 게시글 삭제
    public void deletePosting(Long id, String userName) {

        if (!(userName.equals(exists(id).getUsername()))) {
            throw new InvalidValueException(ErrorCode.NOT_AUTHORIZED);
        }
        postingRepository.deleteById(id);
    }

    private Posting exists(long id) {
        return postingRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(ErrorCode.NOTFOUND_POST));
    }
}
