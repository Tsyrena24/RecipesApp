package com.example.recipesapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)  //когда ошибка ексепшенс смотрит на все анатации этого искл, мы можем передать другой HTTP статус (например 404)
public class RecipeNotFoundException extends RuntimeException {
}
