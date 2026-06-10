package com.splitexpense.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.splitexpense.dto.GroupRequest;
import com.splitexpense.dto.GroupResponse;
import com.splitexpense.dto.UserResponse;
import com.splitexpense.entity.Group;
import com.splitexpense.entity.GroupMember;
import com.splitexpense.entity.User;
import com.splitexpense.repository.GroupMemberRepository;
import com.splitexpense.repository.GroupRepository;
import com.splitexpense.repository.UserRepository;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserService userService;

    //createGroup()    → Group create చేయడం
    public GroupResponse createGroup(GroupRequest request) {
        // Step 1: Creator ఉన్నాడా check చేయి
        User creator = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        // Step 2: Group create చేయి
        Group group = new Group();
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setCreatedBy(creator);
        Group saved = groupRepository.save(group);
        // Step 3: Creator ని automatically member గా add చేయి
        GroupMember creatorMember = new GroupMember();
        creatorMember.setGroup(saved);
        creatorMember.setUser(creator);
        groupMemberRepository.save(creatorMember);
        // Step 4: మిగిలిన members add చేయి
        if (request.getMemberIds() != null) {
            for (Long memberId : request.getMemberIds()) {
                if (!memberId.equals(request.getCreatedBy())) {
                    User member = userRepository.findById(memberId)
                            .orElseThrow(() -> new RuntimeException("Member not found!"));
                    GroupMember gm = new GroupMember();
                    gm.setGroup(saved);
                    gm.setUser(member);
                    groupMemberRepository.save(gm);
                }
            }
        }

        return mapToResponse(saved);
    }
//getGroupById()   → ఒక group details తీసుకోవడం
    public GroupResponse getGroupById(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found!"));
        return mapToResponse(group);
    }
//getGroupsByUser() → ఒక user యొక్క అన్ని groups
    public List<GroupResponse> getGroupsByUser(Long userId) {
        List<GroupMember> memberships = groupMemberRepository.findByUserId(userId);
        return memberships.stream()
                .map(gm -> mapToResponse(gm.getGroup()))
                .collect(Collectors.toList());
    }
//mapToResponse()  → Entity → DTO convert
    public GroupResponse mapToResponse(Group group) {
        GroupResponse response = new GroupResponse();
        response.setId(group.getId());
        response.setName(group.getName());
        response.setDescription(group.getDescription());
        response.setCreatedBy(userService.mapToResponse(group.getCreatedBy()));

        List<GroupMember> members = groupMemberRepository.findByGroupId(group.getId());
        List<UserResponse> memberResponses = members.stream()
                .map(gm -> userService.mapToResponse(gm.getUser()))
                .collect(Collectors.toList());
        response.setMembers(memberResponses);

        return response;
    }
}