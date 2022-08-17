package com.sparta.sorisam.service;

import com.sparta.sorisam.Dto.PostingLikeUserDto;
import com.sparta.sorisam.Dto.RequestDto.PostingRequestDto;
import com.sparta.sorisam.Dto.RequestDto.PostingUpdateRequestDto;
import com.sparta.sorisam.Dto.ResponseDto.PostingDetailResponseDto;
import com.sparta.sorisam.Dto.ResponseDto.PostingResponseDto;
import com.sparta.sorisam.Model.Posting;
import com.sparta.sorisam.Model.PostingLike;
import com.sparta.sorisam.Repository.LikeRepository;
import com.sparta.sorisam.Repository.PostingRepository;
import com.sparta.sorisam.global.error.exception.EntityNotFoundException;
import com.sparta.sorisam.global.error.exception.ErrorCode;
import com.sparta.sorisam.global.error.exception.InvalidValueException;
import com.sparta.sorisam.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PostingService {
    private final PostingRepository postingRepository;

    private final LikeRepository likeRepository;
    private final S3Service s3Service;


    // 게시글 작성
    @Transactional // 메서드의 실행, 종료, 예외를 기준으로 각각 실행(begin), 종료(commit), 예외(rollback)를 자동으로 처리해 준다.
    public Posting createPosting(PostingRequestDto postingRequestDto, String username, String img, String intro, MultipartFile audioFile) {
        String filePath = "";
        if (audioFile != null) {
            filePath = s3Service.uploadAudio(audioFile);
        }

        String title = postingRequestDto.getTitle();
        String contents = postingRequestDto.getContents();
        if (title.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_TITLE);
        } else if (contents.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_CONTENTS);
        } else if (filePath.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_FILEPATH);
        }

        return postingRepository.save(new Posting(postingRequestDto.getTitle(), postingRequestDto.getContents(), filePath, username, img, intro));
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
    public void updatePosting(Long id, PostingUpdateRequestDto dto, String userName, MultipartFile audioFile) {
        if (!(userName.equals(exists(id).getUsername()))) {
            throw new InvalidValueException(ErrorCode.NOT_AUTHORIZED);
        }

        String filePath = "";
        if (audioFile != null) {
            filePath = s3Service.uploadAudio(audioFile);
        }

        String title = dto.getTitle();
        String contents = dto.getContents();

        if (title.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_TITLE);
        } else if (contents.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_CONTENTS);
        } else if (filePath.length() < 1) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_FILEPATH);
        }

        Posting existingPosting = exists(id);
        existingPosting.updatePosting(dto.getTitle(), dto.getContents(), filePath);
        postingRepository.save(existingPosting);
    }

    // 게시글 삭제
    public void deletePosting(Long id, String userName) {
        Posting posting = postingRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(ErrorCode.NOTFOUND_POST));
        if (!(userName.equals(exists(id).getUsername()))) {
            throw new InvalidValueException(ErrorCode.NOT_AUTHORIZED);
        }
        postingRepository.deleteById(id);
        s3Service.deleteObjectByFilePath(posting.getFilePath());
    }

    private Posting exists(long id) {
        return postingRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(ErrorCode.NOTFOUND_POST));
    }
}
