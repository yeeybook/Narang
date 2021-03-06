package com.exp.narang.api.model.service;

import com.exp.narang.api.model.network.ImgbbResponse;
import com.exp.narang.api.model.network.ImgbbResponseData;
import com.exp.narang.api.model.request.UserInfoUpdateReq;
import com.exp.narang.api.model.request.UserRegisterPostReq;
import com.exp.narang.api.model.db.entity.User;
import com.exp.narang.api.model.db.repository.UserRepository;
import com.exp.narang.api.model.db.repository.UserRepositorySupport;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRepositorySupport userRepositorySupport;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private static final OkHttpClient httpClient;
	private static final String KEY = "d3e0947404ef9c6533664b5c536be532";

//	public UserServiceImpl(UserRepository userRepository, UserRepositorySupport userRepositorySupport,
//						   PasswordEncoder passwordEncoder){
//		this.userRepository = userRepository;
//		this.userRepositorySupport = userRepositorySupport;
//		this.passwordEncoder = passwordEncoder;
	static{
		httpClient = new OkHttpClient();
	}
//	}

	/**
	 * 모든 유저 목록을 가져오는 메서드
	 * @return 유저 리스트
	 */
	@Override
	public List<User> findUserList() {
		return userRepository.findAll();
	}

	@Override
	public User createUser(UserRegisterPostReq userRegisterInfo) {
		return userRepository.save(
				User.builder().
						email(userRegisterInfo.getEmail())
						.username(userRegisterInfo.getUsername())
						.password(passwordEncoder.encode(userRegisterInfo.getPassword())) // 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
						.build()
		);
	}

	@Override
	public User getUserByEmail(String email) {
		// 디비에 유저 정보 조회 (email 을 통한 조회).
		Optional<User> userOpt = userRepositorySupport.findUserByEmail(email);
		return userOpt.orElse(null);
	}

	@Override
	public boolean idExists(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public boolean userNameExists(String username) {
		return userRepository.existsByUsername(username);
	}

	@Transactional
	@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public void deleteProfile(User user) {
		String upload_path = ":D/images/profile/";
		try {
			if (user.getThumbnailUrl() != null) {
				File file = new File(upload_path + user.getUserId() + ".jpg");
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setThumbnailUrl(null);
		userRepository.save(user);
	}

	@Override
	public User updateUser(UserInfoUpdateReq updateInfo, User user) {
		String upload_path = "D:/images/profile/";
		//profileImage 설정
		if(updateInfo.getFile() != null) {
//			try {
//				if (user.getThumbnailUrl() != null) {
//					File file = new File(upload_path + user.getUserId() + ".jpg");
//					file.delete();
//				}
//				updateInfo.getFile().transferTo(new File(upload_path + user.getUserId() + ".jpg"));
//				user.setThumbnailUrl("/images/profile/" + user.getUserId() + ".jpg");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			try {
				requestUpload(updateInfo.getFile(), user);
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		if(updateInfo.getUsername() != null) {
			user.setUsername(updateInfo.getUsername());
		}
		if(updateInfo.getCurrentPassword() != null) {
			user.setPassword(passwordEncoder.encode(updateInfo.getNewPassword()));
		}
		return userRepository.save(user);
	}

	private void requestUpload(MultipartFile mFile, User user) throws IOException {
		log.debug("받은 파일명 : " + mFile.getOriginalFilename());

		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("key", KEY)
				.addFormDataPart("image", mFile.getOriginalFilename(), RequestBody.create(MediaType.parse(""), mFile.getBytes()))
				.build();

		Request request = new Request.Builder()
				.url("https://api.imgbb.com/1/upload")
				.post(requestBody)
				.build();

		httpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				e.printStackTrace();
				log.debug("실패");
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
				ImgbbResponse imgbbResponse = new Gson().fromJson(response.body().string(), ImgbbResponse.class);
				ImgbbResponseData data = imgbbResponse.getData();
				if(imgbbResponse.isSuccess()){
					user.setThumbnailUrl(data.getUrl());
					log.debug("업로드한 이미지 url" + data.getUrl());
					log.debug("deleteUrl" + data.getDeleteUrl());
					userRepository.save(user);
				}
			}
		});
	}
}
