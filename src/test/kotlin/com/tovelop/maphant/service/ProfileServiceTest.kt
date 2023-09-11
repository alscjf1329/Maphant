package com.tovelop.maphant.service

import com.tovelop.maphant.dto.BoardResDto
import com.tovelop.maphant.dto.ProfileDto
import com.tovelop.maphant.mapper.ProfileMapper
import com.tovelop.maphant.type.paging.Pagination
import com.tovelop.maphant.type.paging.PagingDto
import com.tovelop.maphant.type.paging.PagingResponse
import com.tovelop.maphant.utils.UploadUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProfileServiceTest {

    @Mock
    private lateinit var uploadUtils: UploadUtils

    @Mock
    private lateinit var profileMapper: ProfileMapper

    @Mock
    private lateinit var uploadLogService: UploadLogService


    @InjectMocks
    private lateinit var profileService: ProfileService

    @Test
    @DisplayName("getProfileImage함수 호출시 ProfileImageDto를 반환해야한다.")
    fun getProfileImageTest() {
//        //given
//        val userId = 1;
//        val expectedProfileImage = ProfileImageDto("test","imageUrl")
//        whenever(profileMapper.findImageById(userId)).thenReturn(expectedProfileImage);
//
//        //when
//        val result = profileService.getProfileImage(userId)
//
//        //then
//        assert(result == expectedProfileImage)
//        verify(profileMapper).findImageById(userId)
    }

    @Test
    @DisplayName("로그인한 유저가 작성한 게시글이 1미만일 경우 PagingResponse에 빈배열과 null값이 들어간다.")
    fun getBoardsListWithNoCount() {
//        //given
//        val userId = 1;
//        val params = PagingDto(1,10)
//        val count = 0
//        val expectedResult = PagingResponse<BoardResDto>(Collections.emptyList(),null);
//        whenever(profileMapper.getBoardCount(userId)).thenReturn(count)
//
//        //when
//        val result = profileService.getBoardsList(userId,params);
//
//        //then
//        assert(result.list == expectedResult.list)
//        assert(result.pagination == expectedResult.pagination)
//        verify(profileMapper, times(1)).getBoardCount(userId)
//        verify(profileMapper, times(0)).findAllBoardByIdWithPaging(userId,params);
    }

    @Test
    @DisplayName("로그인한 유저가 작성한 게시글이 1 이상일 경우 PagingResponse에 boards과 Pagination값이 들어간다.")
    fun getBoardListWithExistCount(){
        //given
//        val userId = 1;
//        val params = PagingDto(1,  10)
//        val count = 2;
//        val boards:List<BoardResDto> = listOf(BoardResDto(
//            1,
//            1,
//            1,
//            "d",
//            "g",
//            "b",
//            1,
//            true,
//            false,
//            LocalDateTime.now(),
//            LocalDateTime.now(),
//            1,
//            1,
//            1,
//            "url"))
//
//        whenever(profileMapper.findAllBoardByIdWithPaging(userId, params)).thenReturn(boards)
//        val expectResult = PagingResponse(boards, Pagination(count, params))
//        whenever(profileMapper.getBoardCount(userId)).thenReturn(count)
//
//        //when
//        val result = profileService.getBoardsList(userId, params)
//
//        //then
//        assert(result.list == expectResult.list)
//        assert(result.pagination == expectResult.pagination)
//        verify(profileMapper, times(1)).getBoardCount(userId)
//        verify(profileMapper, times(1)).findAllBoardByIdWithPaging(userId,params)
    }

    @Test
    @DisplayName("파라미터로 들어온 file이 png, jpeg, jpg에 해당하지 않는다면 IllegalArgumentException에러를 던진다.")
    fun updateProfileImageWithFileNotPngJpegJpg() {
        //given
        val userId = 1;
        val imageUrl = "imageurl"
        val file:MultipartFile = MockMultipartFile(
            "file", // 파일 이름
            "test.txt", // 원본 파일 이름
            "text/plain", // 컨텐츠 타입
            "test".toByteArray(StandardCharsets.UTF_8) // 파일의 내용
        )
        whenever(uploadUtils.isNotImageFile(file.originalFilename as String)).thenReturn(true)

        //when
        val exception = assertThrows<IllegalArgumentException> {
            profileService.updateProfileImage(userId, imageUrl, file)
        }

        //then
        assertEquals("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.",exception.message)
        verify(uploadUtils, times(1)).isNotImageFile(file.originalFilename as String);
    }

    @Test
    @DisplayName("해당 유저의 id값을 fk로 갖는 Profile이 있는경우 프로필 image를 업데이트한다.")
    fun updateProfileImage() {
        //given
        val userId = 1;
        val imageUrl = "imageurl"
        val file: MultipartFile = MockMultipartFile(
            "test.jpeg", "test.jpeg", "byte",
            "test".toByteArray()
        )
        val profile = ProfileDto(userId,imageUrl,null);
        whenever(uploadUtils.isNotImageFile(file.originalFilename as String)).thenReturn(false)
        whenever(profileMapper.findById(userId)).thenReturn(profile);

        //when
        val result = profileService.updateProfileImage(userId,imageUrl,file);

        //then
        assert(result == imageUrl);
        verify(profileMapper, times(0)).insertProfile(userId, imageUrl);
        verify(profileMapper).updateProfileImage(userId, imageUrl);
    }

    @Test
    @DisplayName("해당 유저의 id값을 fk로 갖는 Profile이 없는경우 Profile 튜플을 insert한다.")
    fun insertProfileImage() {
        //given
        val userId = 1;
        val imageUrl = "imageurl"
        val file: MultipartFile = MockMultipartFile(
            "test.jpeg", "test.jpeg", "byte",
            "test".toByteArray()
        )

        whenever(uploadUtils.isNotImageFile(file.originalFilename as String)).thenReturn(false)
        whenever(profileMapper.findById(userId)).thenReturn(null);

        //when
        val result = profileService.updateProfileImage(userId,imageUrl,file);

        //then
        assert(result == imageUrl);
        verify(profileMapper, times(0)).updateProfileImage(userId, imageUrl);
        verify(profileMapper).insertProfile(userId, imageUrl);
    }
}
