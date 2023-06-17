package com.co.softworld.reactive.programming.section2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UserComment {
    private User user;
    private Comment comment;
}