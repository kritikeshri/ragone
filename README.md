### Rag One repo explores the different aspects of RAG with Java

# One of the imp output schemas for future ref

```{
        "$schema": "https://json-schema.org/draft/2020-12/schema",
        "$id": "https://example.invalid/schemas/vellum-quay-rag-answer.schema.json",
        "title": "Vellum Quay Structured RAG Answer",
        "description": "A moderately complex output contract for testing retrieval-grounded answers. Null is allowed where the source lacks evidence; claims must not be invented.",
        "type": "object",
        "additionalProperties": false,
        "required": [
            "answer_status",
            "summary",
            "facts",
            "evidence",
            "missing_information",
            "conflicts",
            "confidence",
            "abstention_reason"
        ],
        "properties": {
            "answer_status": {
                "type": "string",
                "enum": [
                    "answered",
                    "partially_answered",
                    "insufficient_evidence",
                    "conflicting_evidence"
                ]
            },
            "summary": {
                "type": "string",
                "maxLength": 500
            },
            "facts": {
                "type": "array",
                "minItems": 0,
                "maxItems": 8,
                "items": {
                    "type": "object",
                    "additionalProperties": false,
                    "required": [
                        "field",
                        "value",
                        "supported"
                    ],
                    "properties": {
                        "field": {
                            "type": "string",
                            "minLength": 1,
                            "maxLength": 80
                        },
                        "value": {
                            "type": "string",
                            "description": "The fact value represented as text."
                        },
                        "supported": {
                            "type": "boolean"
                        }
                    }
                }
            },
            "evidence": {
                "type": "array",
                "maxItems": 10,
                "items": {
                    "type": "object",
                    "additionalProperties": false,
                    "required": [
                        "source_section",
                        "claim_supported",
                        "quote_or_paraphrase"
                    ],
                    "properties": {
                        "source_section": {
                            "type": "string",
                            "minLength": 1,
                            "maxLength": 120
                        },
                        "claim_supported": {
                            "type": "string",
                            "minLength": 1,
                            "maxLength": 240
                        },
                        "quote_or_paraphrase": {
                            "type": "string",
                            "minLength": 1,
                            "maxLength": 400
                        }
                    }
                }
            },
            "missing_information": {
                "type": "array",
                "maxItems": 8,
                "items": {
                    "type": "string",
                    "minLength": 1,
                    "maxLength": 200
                }
            },
            "conflicts": {
                "type": "array",
                "maxItems": 5,
                "items": {
                    "type": "object",
                    "additionalProperties": false,
                    "required": [
                        "topic",
                        "values",
                        "resolution"
                    ],
                    "properties": {
                        "topic": {
                            "type": "string",
                            "minLength": 1,
                            "maxLength": 120
                        },
                        "values": {
                            "type": "array",
                            "minItems": 2,
                            "maxItems": 4,
                            "items": {
                                "type": "object",
                                "additionalProperties": false,
                                "required": [
                                    "value",
                                    "source_id"
                                ],
                                "properties": {
                                    "value": {
                                        "type": "string"
                                    },
                                    "source_id": {
                                        "type": "string",
                                        "minLength": 1,
                                        "maxLength": 80
                                    }
                                }
                            }
                        },
                        "resolution": {
                            "type": "string",
                            "enum": [
                                "resolved_by_revision",
                                "unresolved",
                                "not_applicable"
                            ]
                        }
                    }
                }
            },
            "confidence": {
                "type": "number",
                "minimum": 0,
                "maximum": 1
            },
            "abstention_reason": {
                "type": "string",
                "maxLength": 300
            }
        },
        "allOf": [
            {
                "if": {
                    "properties": {
                        "answer_status": {
                            "const": "answered"
                        }
                    },
                    "required": [
                        "answer_status"
                    ]
                },
                "then": {
                    "properties": {
                        "abstention_reason": {
                            "type": "string"
                        }
                    }
                }
            },
            {
                "if": {
                    "properties": {
                        "answer_status": {
                            "const": "insufficient_evidence"
                        }
                    },
                    "required": [
                        "answer_status"
                    ]
                },
                "then": {
                    "properties": {
                        "abstention_reason": {
                            "type": "string",
                            "minLength": 1
                        }
                    }
                }
            },
            {
                "if": {
                    "properties": {
                        "answer_status": {
                            "const": "conflicting_evidence"
                        }
                    },
                    "required": [
                        "answer_status"
                    ]
                },
                "then": {
                    "properties": {
                        "conflicts": {
                            "minItems": 1
                        }
                    }
                }
            }
        ]
    }```